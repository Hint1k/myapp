package com.example.myapp.controller;

import com.example.myapp.entity.FileDb;
import com.example.myapp.service.CourierService;
import com.example.myapp.service.FileService;
import com.example.myapp.testData.FileData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testUploadFileToDb() {
        int id = 2; // file "13.txt" has index 2 in the array "files"

        // creating a virtual file
        byte[] fileData = "nice-information-3".getBytes(); // file's content
        MockMultipartFile file = new MockMultipartFile(
                "file", "13.txt",
                "text/plain", fileData);

        // success message
        String message = "The file is uploaded successfully: 13.txt";

        when(fileService.saveFile(file)).thenReturn(files.get(id));

        // testing
        try {
            mockMvc.perform(multipart("/api/files/upload")
                            .file(file)
                            .with(csrf())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(message))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testUploadFileToDb() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).saveFile(file);
    }

    @Test
    public void testGetListOfFilesFromDbAsHtml() {
        when(fileService.getAllFiles()).thenReturn(files.stream());

        // testing
        try {
            mockMvc.perform(get("/api/files/html"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("download-delete"))
                    .andExpect(model().attribute("files", hasSize(5)))
                    .andExpect(content().string(containsString("14.txt")))
                    .andExpect(content().string(containsString("15.txt")))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testGetListOfFilesFromDbAsHtml() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getAllFiles();
    }

    @Test
    public void testGetListOfFilesFromDbAsJson() {
        when(fileService.getAllFiles()).thenReturn(files.stream());

        // parsing the response in json format
        String name = "$.[?(@.name == '11.txt')].name";
        String type = "$.[?(@.name == '12.txt')].type";
        String size = "$.[?(@.name == '13.txt')].size";

        // testing
        try {
            mockMvc.perform(get("/api/files/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(name).value("11.txt"))
                    .andExpect(jsonPath(type).value("text/plain"))
                    .andExpect(jsonPath(size).value(18)) // bytes
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testGetListOfFilesFromDbAsJson() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getAllFiles();
    }

    @Test
    public void testDownloadFileFromDbById() {
        int fileId = 11; // file "11.txt" has database id = 11
        int id = 0; // file "11.txt" has index 0 in the array "files"
        byte[] fileData = "information-1".getBytes(); // file's content

        when(fileService.getById(fileId))
                .thenReturn(Optional.ofNullable(files.get(id)));

        // testing
        try {
            mockMvc.perform(get("/api/files/json/{id}", fileId))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(fileData))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testDownloadFileFromDbById() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getById(fileId);
    }

    @Test
    public void testDownloadFileFromDbByName() {
        String name = "12.txt"; // file name
        int id = 1; // file "12.txt" has index 1 in the array "files"
        byte[] fileData = "important-info-2".getBytes(); // file's content

        when(fileService.getByName(name)).thenReturn(files.get(id));

        // testing
        try {
            mockMvc.perform(get("/api/files/html/{name}", name))
                    .andExpect(status().isOk())
                    .andExpect(content().bytes(fileData))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testDownloadFileFromDbByName() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getByName(name);
    }

    @Test
    public void testDeleteFileFromDbByName() {
        int id = 3; // delete file "14.txt" with id = 3
        FileDb file = files.get(id);
        String name = file.getName();

        when(fileService.getByName(name)).thenReturn(file);
        doNothing().when(fileService).deleteFile(name);

        //testing
        try {
            mockMvc.perform(delete("/api/files/html/{name}", name)
                            .with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/api/files/html"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testDeleteFileFromDbByName() fails");
            throw new RuntimeException(e);
        }

        verify(fileService, times(1)).getByName(name);
        verify(fileService, times(1)).deleteFile(name);
    }
}