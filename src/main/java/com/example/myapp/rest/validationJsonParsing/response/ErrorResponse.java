package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty("error")
    private ErrorDetails error;

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }
}
