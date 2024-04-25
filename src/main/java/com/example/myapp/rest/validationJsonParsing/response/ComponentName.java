package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentName {

    @JsonProperty("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}