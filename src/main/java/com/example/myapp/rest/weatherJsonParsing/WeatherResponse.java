package com.example.myapp.rest.weatherJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponse {
    @JsonProperty("coord")
    private Coordinates coordinates;
    @JsonProperty("weather")
    private Weather[] weather;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("name")
    private String name;

    public WeatherResponse() {
    }

    public Coordinates getPosition() {
        return coordinates;
    }

    public void setPosition(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}