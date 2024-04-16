package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.AddressComponent;

import java.util.List;

public class ResponseAddress {

    @JsonProperty("formattedAddress")
    private String formattedAddress;

    @JsonProperty("postalAddress")
    private PostalAddress postalAddress;

    @JsonProperty("addressComponents")
    private List<AddressComponent> addressComponents;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }
}