package com.example.myapp.service;

import com.example.myapp.dao.FileDao;
import com.example.myapp.entity.FileDb;
import com.example.myapp.testData.FileData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

// unit testing of service layer
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileServiceImpl;

    @Mock
    private FileDao fileDao;

    private static List<FileDb> files;

    @BeforeAll
    public static void createCustomerList() {
        FileData fileData = new FileData();
        files = fileData.getFileData();
    }

    @Test
    public void testSaveFile() {
        int id = 1;
        FileDb fileDb = files.get(id);

        // creating a virtual file
        byte[] fileData = "information-1".getBytes(); // file's content
        MockMultipartFile file = new MockMultipartFile(
                "file", "11.txt",
                "text/plain", fileData);

        when(fileDao.save(any(FileDb.class))).thenReturn(fileDb);

        // testing
        fileServiceImpl.saveFile(file);

        verify(fileDao, times(1)).save(any(FileDb.class));
    }

    @Test
    public void testGetById() {
        int id = 2;
        FileDb file = files.get(id);

        when(fileDao.findById(String.valueOf(id))).thenReturn(Optional.ofNullable(file));

        // testing
        fileServiceImpl.getById(id);

        verify(fileDao, times(1)).findById(String.valueOf(id));
    }

    @Test
    public void testGetByName() {
        int id = 3;
        FileDb file = files.get(id);
        String name = file.getName();

        when(fileDao.findByName(name)).thenReturn(file);

        // testing
        fileServiceImpl.getByName(name);

        verify(fileDao, times(1)).findByName(name);
    }

    @Test
    public void testGetAllFiles() {
        when(fileDao.findAll()).thenReturn(files);

        // testing
        fileServiceImpl.getAllFiles();

        verify(fileDao, times(1)).findAll();
    }
}