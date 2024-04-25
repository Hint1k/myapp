package com.example.myapp.service;

import com.example.myapp.entity.FileDb;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Stream;

public interface FileService {

    FileDb saveFile(MultipartFile file);

    Optional<FileDb> getById(Integer id);

    FileDb getByName(String name);

    Stream<FileDb> getAllFiles();

    void deleteFile(String name);
}