package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationResult {

    @JsonProperty("address")
    private ResponseAddress responseAddress;

    public ResponseAddress getResponseAddress() {
        return responseAddress;
    }

    public void setResponseAddress(ResponseAddress responseAddress) {
        this.responseAddress = responseAddress;
    }
}