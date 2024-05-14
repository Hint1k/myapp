package com.example.myapp.controller;

import com.example.myapp.entity.Courier;
import com.example.myapp.entity.FileDb;
import com.example.myapp.file.DiskSpaceUtil;
import com.example.myapp.file.FileInfo;
import com.example.myapp.file.ResponseFile;
import com.example.myapp.service.CourierService;
import com.example.myapp.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private CourierService courierService;

    private static final Logger logger
            = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/files/upload") // "upload" here is a noun
    public String uploadFileToDb(
            @RequestParam("file") MultipartFile file, Model model) {

        String result = performFileUploadChecks(file, model);
        if (result != null) {
            return result;
        }

        fileService.saveFile(file);
        String message = "The file is uploaded successfully: "
                + file.getOriginalFilename();
        model.addAttribute("successMessage", message);
        return "upload-form";
    }

    private String performFileUploadChecks(MultipartFile file, Model model) {

        // Checking that user first chose a file and then tried to upload it
        if (file.isEmpty()) {
            String message = "Please select a file to upload.";
            model.addAttribute("errorMessage", message);
            return "upload-form";
        }

        // Checking that the file name is a number or number.extension
        String fullFileName = Objects.requireNonNull(file.getOriginalFilename());
        String[] fileName = fullFileName.split("\\.");
        int courierId = 0;
        try {
            courierId = Integer.parseInt(fileName[0]);
        } catch (NumberFormatException e) {
            String message = "File name must be a number or number.extension";
            model.addAttribute("errorMessage", message);
            return "upload-form";
        }

        // Checking that the filename is an existing courier id
        List<Courier> couriers = courierService.getCouriers();
        List<Integer> idList = new ArrayList<>();
        for (Courier courier : couriers) {
            int id = courier.getId();
            idList.add(id);
        }

        if (!idList.contains(courierId)) {
            String message = "File name must be an existing courier id";
            model.addAttribute("errorMessage", message);
            return "upload-form";
        }

        // Checking the file size
        long maxSizeInBytes = 1024 * 1024; // 1MB in bytes
        long fileSizeInBytes = file.getSize();
        if (fileSizeInBytes > maxSizeInBytes) {
            String message = "File size exceeds the maximum allowed limit (1MB).";
            model.addAttribute("errorMessage", message);
            return "upload-form";
        }

        return null; // if no error detected
    }

    @GetMapping("/files/html")
    public String getListOfFilesFromDbAsHtml(Model model) {
        List<FileInfo> files = fileService.getAllFiles().map(
                file -> {
                    String fileName = file.getName();
                    String fileUrl = MvcUriComponentsBuilder
                            .fromMethodName(FileController.class,
                                    "downloadFileFromDbByName",
                                    file.getName()).build().toString();
                    return new FileInfo(fileName, fileUrl);
                }
        ).toList();

        model.addAttribute("files", files);

        return "download-delete";
    }

    @GetMapping("/files/json")
    public ResponseEntity<List<ResponseFile>> getListOfFilesFromDbAsJson() {
        List<ResponseFile> files = fileService
                .getAllFiles().map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api/files/json/")
                            .path(String.valueOf(file.getId()))
                            .toUriString();

                    return new ResponseFile(
                            file.getName(),
                            file.getType(),
                            fileDownloadUri,
                            file.getData().length);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/json/{id}")
    public ResponseEntity<byte[]> downloadFileFromDbById(
            @PathVariable Integer id) {

        Optional<FileDb> file = fileService.getById(id);
        ResponseEntity<byte[]> responseEntity = checkDiskSpace(file);
        return handleResponse(responseEntity, file);
    }

    @GetMapping("/files/html/{name}")
    public ResponseEntity<byte[]> downloadFileFromDbByName(
            @PathVariable String name) {

        FileDb file = fileService.getByName(name);
        ResponseEntity<byte[]> responseEntity
                = checkDiskSpace(Optional.ofNullable(file));
        return handleResponse(responseEntity, Optional.ofNullable(file));
    }

    private ResponseEntity<byte[]> checkDiskSpace(Optional<FileDb> file) {

        if (file.isPresent()) {
            byte[] fileData = file.get().getData();
            if (!DiskSpaceUtil.isEnoughDiskSpace(fileData.length)) {
                String errorMessage = "Not enough space on your device.";
                return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE)
                        .body(errorMessage.getBytes());
            }
        }
        // if there is enough disk space
        return null;
    }

    private ResponseEntity<byte[]> handleResponse(
            ResponseEntity<byte[]> responseEntity, Optional<FileDb> file) {

        if (responseEntity != null) {
            return responseEntity;
        }
        return file.map(value -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename = "
                                + value.getName())
                .body(value.getData())).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/files/html/{name}")
    public String deleteFileFromDbByName(@PathVariable String name) {
        FileDb file = fileService.getByName(name);
        if (file != null) {
            fileService.deleteFile(name);
        }
        return "redirect:/api/files/html";
    }
}