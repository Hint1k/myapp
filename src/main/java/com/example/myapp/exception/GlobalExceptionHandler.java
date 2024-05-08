package com.example.myapp.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handling invalid Google responses
    @ExceptionHandler(InvalidGoogleResponse.class)
    public String handleInvalidGoogleResponse(InvalidGoogleResponse ex, Model model) {

        String errorMessage = "The application encountered an invalid response from Google.";
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    // handling Google server not responding case
    @ExceptionHandler(HttpServerErrorException.class)
    public String handleGoogleServerNotResponding(
            HttpServerErrorException ex, Model model) {

        String errorMessage = "Google server is not responding.";
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    // handling generic exceptions
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {

        String errorMessage = ex.getMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}