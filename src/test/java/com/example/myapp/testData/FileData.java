package com.example.myapp.testData;

import com.example.myapp.entity.FileDb;

import java.util.ArrayList;
import java.util.List;

public class FileData {

    private List<FileDb> files;

    private void createFileList(){
        files = new ArrayList<>();

        byte[] data1 = "information-1".getBytes();
        byte[] data2 = "important-info-2".getBytes();
        byte[] data3 = "nice-information-3".getBytes();
        byte[] data4 = "important-information-4".getBytes();
        byte[] data5 = "mega-important-information-5".getBytes();

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