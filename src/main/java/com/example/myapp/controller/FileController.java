package com.example.myapp.controller;

import com.example.myapp.entity.FileDb;
import com.example.myapp.entity.FileInfo;
import com.example.myapp.response.ResponseFile;
import com.example.myapp.response.ResponseMessage;
import com.example.myapp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/files/upload") // "upload" here is a noun
    public ResponseEntity<ResponseMessage> uploadFileToDb(
            @RequestParam("file") MultipartFile file) {

        // to prevent user attempt to add "no file" record to database
        if (file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Please select a file to upload."));
        }

        String message = "";
        try {
            fileService.saveFile(file);
            message = "The file is uploaded successfully: "
                    + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(message));
        } catch (Exception exception) {
            message = "Could not upload the file: "
                    + file.getOriginalFilename();
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
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

        return file.map(value -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename = "
                                + value.getName())
                .body(value.getData())).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/files/html/{name}")
    public ResponseEntity<byte[]> downloadFileFromDbByName(
            @PathVariable String name) {

        FileDb file = fileService.getByName(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename = "
                                + file.getName())
                .body(file.getData());
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