package com.example.myapp.rest.validationJsonParsing.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationRequest {

    @JsonProperty("address")
    private RequestAddress requestAddress;

    public RequestAddress getRequestAddress() {
        return requestAddress;
    }

    public void setRequestAddress(RequestAddress requestAddress) {
        this.requestAddress = requestAddress;
    }
}