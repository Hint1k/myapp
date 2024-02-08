package com.example.myapp.rest.locationJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationResponse {
    @JsonProperty("results")
    private Result[] results;

    public LocationResponse(){}

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }
}