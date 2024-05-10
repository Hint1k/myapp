package com.example.myapp.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;

public class DiskSpaceUtil {

    private static final Logger logger
            = LoggerFactory.getLogger(DiskSpaceUtil.class);

    public static boolean isEnoughDiskSpace(long fileSize) {
        try {
            Iterable<FileStore> fileStores = FileSystems.getDefault().getFileStores();
            for (FileStore store : fileStores) {
                long usableSpace = store.getUsableSpace();
                if (usableSpace >= fileSize) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return false;
    }
}