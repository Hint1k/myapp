package com.example.myapp.service;

import com.example.myapp.dao.FileDao;
import com.example.myapp.entity.FileDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    private static final Logger logger
            = LoggerFactory.getLogger(FileServiceImpl.class);

    public FileDb saveFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));

        List<FileDb> files = fileDao.findAll().stream().toList();

        Integer id = -1;
        for (int i = 0; i < files.size(); i++) {
            FileDb fileDb = files.get(i);
            if (file.getOriginalFilename().equalsIgnoreCase(fileDb.getName())) {
                id = fileDb.getId();
            }
        }

        FileDb newFileDb;
        try {
            if (id != -1) {
                // merging file with the same name to database
                newFileDb = new FileDb(id, fileName, file.getContentType(), file.getBytes());
            } else {
                // saving new file to database
                newFileDb = new FileDb(fileName, file.getContentType(), file.getBytes());
            }
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return fileDao.save(newFileDb);
    }

    public Optional<FileDb> getById(Integer id) {
        return fileDao.findById(String.valueOf(id));
    }

    public FileDb getByName(String name) {
        return fileDao.findByName(name);
    }

    public Stream<FileDb> getAllFiles() {
        return fileDao.findAll().stream();
    }

    @Override
    public void deleteFile(String name) {
        FileDb file = fileDao.findByName(name);
        if (file != null) {
            fileDao.delete(file);
        }
    }
}