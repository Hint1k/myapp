package com.example.myapp.controller;

import com.example.myapp.entity.FileDb;
import com.example.myapp.response.ResponseFile;
import com.example.myapp.response.ResponseMessage;
import com.example.myapp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload") // "upload" here is a noun
    public ResponseEntity<ResponseMessage> uploadFileToDb(
            @RequestParam("file") MultipartFile file) {
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

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListOfFilesFromDb() {
        List<ResponseFile> files = fileService
                .getAllFiles().map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/")
                            .path(String.valueOf(file.getId()))
                            .toUriString();

                    return new ResponseFile(
                            file.getName(),
                            fileDownloadUri,
                            file.getType(),
                            file.getData().length);
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> downloadFileFromDb(
            @PathVariable Integer id) {

        Optional<FileDb> file = fileService.getById(id);

        return file.map(value -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename = "
                                + value.getName())
                .body(value.getData())).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}