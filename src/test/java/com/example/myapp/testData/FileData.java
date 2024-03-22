package com.example.myapp.testData;

import com.example.myapp.entity.FileDb;

import java.util.ArrayList;
import java.util.List;

public class FileData {

    private List<FileDb> files;

    private void createFileList(){
        files = new ArrayList<>();

        byte[] data1 = "file1.txt".getBytes();
        byte[] data2 = "file2.pdf".getBytes();
        byte[] data3 = "file3.jpg".getBytes();
        byte[] data4 = "file4.doc".getBytes();
        byte[] data5 = "file5.gif".getBytes();

        FileDb file1 = new FileDb(11,"11.txt","text/plain", data1);
        FileDb file2 = new FileDb(12,"12.txt","text/plain", data2);
        FileDb file3 = new FileDb(13,"13.txt","text/plain", data3);
        FileDb file4 = new FileDb(14,"14.txt","text/plain", data4);
        FileDb file5 = new FileDb(15,"15.txt","text/plain", data5);

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
    }

    public FileData() {
        createFileList();
    }

    public List<FileDb> getFileData() {
        return files;
    }

    public void setFileData(List<FileDb> files) {
        this.files = files;
    }
}