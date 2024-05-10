package com.example.myapp.exception;

/**
 * This is to catch error when OpenWeatherMap.org API returns a response that cannot be processed
 * by the application due to unexpected format, missing data, or other issues. It could indicate
 * a problem with the API itself or with the application's handling of the response.
 */
public class InvalidWeatherResponse extends RuntimeException {

    public InvalidWeatherResponse(String message) {
        super(message);
    }
}
