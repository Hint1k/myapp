package com.example.myapp.rest.weatherJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("description")
    private String description;

    public Weather() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}