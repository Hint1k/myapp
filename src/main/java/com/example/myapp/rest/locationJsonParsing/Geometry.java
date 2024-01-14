package com.example.myapp.rest.locationJsonParsing;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Geometry {

    @JsonProperty("location")
    private Location location;

    public Geometry() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
