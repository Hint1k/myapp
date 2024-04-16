package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostalAddress {

    @JsonProperty("regionCode")
    String regionCode;

    @JsonProperty("locality")
    String locality;

    @JsonProperty("addressLine")
    List<String> addressLine;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public List<String> getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(List<String> addressLine) {
        this.addressLine = addressLine;
    }
}