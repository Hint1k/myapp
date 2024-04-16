package com.example.myapp.rest.validationJsonParsing.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestAddress {

    @JsonProperty("regionCode")
    private String regionCode;

    @JsonProperty("locality")
    private String locality;

    @JsonProperty("addressLines")
    private List<String> addressLines;

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

    public List<String> getAddressLines() {
        return addressLines;
    }

    public void setAddressLines(List<String> addressLines) {
        this.addressLines = addressLines;
    }
}