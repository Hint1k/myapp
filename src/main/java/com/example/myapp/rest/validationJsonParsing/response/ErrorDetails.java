package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDetails {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}