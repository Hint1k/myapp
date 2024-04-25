package com.example.myapp.rest.validationJsonParsing.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressComponent {

    @JsonProperty("componentName")
    private ComponentName componentName;

    @JsonProperty("componentType")
    private String componentType;

    @JsonProperty("confirmationLevel")
    private String confirmationLevel;

    public ComponentName getComponentName() {
        return componentName;
    }

    public void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getConfirmationLevel() {
        return confirmationLevel;
    }

    public void setConfirmationLevel(String confirmationLevel) {
        this.confirmationLevel = confirmationLevel;
    }
}