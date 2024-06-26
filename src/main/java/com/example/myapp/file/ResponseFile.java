package com.example.myapp.file;

public class ResponseFile {
    private String name;
    private String type;
    private String url;
    private long size;

    public ResponseFile() {
    }

    public ResponseFile(String name, String type,
                        String url, long size) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}