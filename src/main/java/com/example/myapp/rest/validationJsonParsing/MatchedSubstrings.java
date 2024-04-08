package com.example.myapp.rest.validationJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchedSubstrings {

    @JsonProperty("length")
    private int length;

    @JsonProperty("offset")
    private int offset;

    public MatchedSubstrings() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}