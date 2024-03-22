package com.example.myapp.controller;

import com.example.myapp.entity.FileDb;
import com.example.myapp.service.CourierService;
import com.example.myapp.service.FileService;
import com.example.myapp.testData.FileData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

// unit test of controller layer
@WebMvcTest(FileController.class)
@WithMockUser // spring security requires a user with a password
public class FileControllerTest {

    @MockBean
    private CourierService courierService;

    @MockBean
    private FileService fileService;

    @Autowired
    private MockMvc mockMvc;

    private List<FileDb> files;

    private static List<FileDb> filesStatic;

    @BeforeAll
    public static void createFileList() {
        FileData fileData = new FileData();
        filesStatic = fileData.getFileData();
    }

    @BeforeEach
    public void setupFiles() {
        // in case of any changes
        files = filesStatic;
    }

    @Test
    public void testGetListOfFilesFromDbAsHtml() {
        when(fileService.getAllFiles()).thenReturn(files.stream());

        // testing
        try {
            mockMvc.perform(get("/api/files/html"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("download-form"))
                    .andExpect(model().attribute("files", hasSize(5)))
                    .andDo(print())
                    .andReturn();

            // I will try to add Assert here later

        } catch (Exception e) {
            System.out.println("testGetListOfFilesFromDbAsHtml() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getAllFiles();
    }
}