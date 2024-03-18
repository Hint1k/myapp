package com.example.myapp.service;

import com.example.myapp.dao.FileDao;
import com.example.myapp.entity.FileDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileService {

    @Autowired
    FileDao fileDao;

    public FileDb saveFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));

        FileDb newFileDb;
        try {
            newFileDb = new FileDb(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            System.out.println("File is not available");
            throw new RuntimeException(e);
        }

        return fileDao.save(newFileDb);
    }

    public Optional<FileDb> getById(Integer id){
        return fileDao.findById(String.valueOf(id));
    }

    public Stream<FileDb> getAllFiles(){
        return fileDao.findAll().stream();
    }
}