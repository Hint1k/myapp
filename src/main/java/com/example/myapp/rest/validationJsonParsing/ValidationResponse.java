package com.example.myapp.rest.validationJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ValidationResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("predictions")
    private List<Predictions> predictions;

    public ValidationResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }
}