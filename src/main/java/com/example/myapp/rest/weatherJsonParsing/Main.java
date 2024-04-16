package com.example.myapp.rest.weatherJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Main {

    @JsonProperty("temp")
    private double temperature;

    @JsonProperty("feels_like")
    private double feelsLike;

    public Main() {
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}