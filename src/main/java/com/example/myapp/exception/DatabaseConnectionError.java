package com.example.myapp.exception;

/**
 This is to catch error occurs while attempting to establish a connection to the database.
 It may indicate issues such as network problems or database server downtime.
 */
public class DatabaseConnectionError extends RuntimeException {

    public DatabaseConnectionError(String message) {
        super(message);
    }
}