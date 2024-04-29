package com.example.myapp.rest.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.validationJsonParsing.request.RequestAddress;
import com.example.myapp.rest.validationJsonParsing.request.ValidationRequest;
import com.example.myapp.rest.validationJsonParsing.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ValidationRestController {

    private final String googleApiKey;

    private final String googleApiUrl;

    private final ObjectMapper objectMapper;

    public ValidationRestController(@Value("${google.api.key}")
                                    String googleApiKey,
                                    @Value("${validation.api.url}")
                                    String googleApiUrl,
                                    ObjectMapper objectMapper) {
        this.googleApiKey = googleApiKey;
        this.googleApiUrl = googleApiUrl;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/customers/order")
    public String validateAddress(@ModelAttribute("address") Address address,
                                  Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "order-form";
        }

        if (!isValidCountryCode(address)) {
            bindingResult.rejectValue("countryName",
                    "countryCode.length",
                    "Country code must be exactly two letters.");
            return "order-form";
        }

        ValidationRequest validationRequest = createValidationRequest(address);
        ResponseEntity<String> response = sendValidationRequest(validationRequest);

        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            if (response != null) {
                String errorMessage = response.getBody();
                model.addAttribute("errorMessage", errorMessage);
                return "order-form";
            }
            return "redirect:/error";
        }

        try {
            ValidationResponse validationResponse = objectMapper
                    .readValue(response.getBody(), ValidationResponse.class);
            handleValidationResponse(validationResponse, address, model);
            return "order-form";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    private boolean isValidCountryCode(Address address) {
        String countryCode = address.getCountryName();
        return countryCode != null && countryCode.length() == 2;
    }

    private ValidationRequest createValidationRequest(Address address) {
        // a house number should always be before a street name
        String addressLines = address.getHouseNumber() + " "
                + address.getStreetName();
        String regionCode = address.getCountryName();
        String locality = address.getCityName();

        RequestAddress requestAddress = new RequestAddress();
        requestAddress.setRegionCode(regionCode);
        requestAddress.setLocality(locality);
        requestAddress.setAddressLines(List.of(addressLines));

        ValidationRequest validationRequest = new ValidationRequest();
        validationRequest.setRequestAddress(requestAddress);

        return validationRequest;
    }

    private ResponseEntity<String> sendValidationRequest(
            ValidationRequest validationRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = googleApiUrl + "/v1:validateAddress?key=" + googleApiKey;
        HttpEntity<String> entity
                = new HttpEntity<>(toJsonString(validationRequest), headers);

        try {
            return new RestTemplate().postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            return handleBadRequestException(ex);
        } catch (Exception ex) {
            return null;
        }
    }

    private String toJsonString(Object object) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, object);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResponseEntity<String> handleBadRequestException(
            HttpClientErrorException.BadRequest ex) {

        /* handling a response from Google when a customer correctly entered
        two letters country code, but it is an invalid country code  */
        try {
            ErrorResponse errorResponse = objectMapper.readValue(ex
                    .getResponseBodyAsString(), ErrorResponse.class);
            return ResponseEntity.badRequest().body(errorResponse.getError().getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred.");
        }
    }

    private void handleValidationResponse(
            ValidationResponse validationResponse, Address address, Model model) {

        ValidationResult validationResult = validationResponse.getValidationResult();
        ResponseAddress responseAddress = validationResult.getResponseAddress();
        String formattedAddress = responseAddress.getFormattedAddress();
        List<AddressComponent> addressComponents
                = responseAddress.getAddressComponents();

        for (AddressComponent component : addressComponents) {
            String name = component.getComponentName().getText();
            switch (component.getComponentType()) {
                case "country":
                    address.setCountryName(name);
                    break;
                case "route":
                    address.setStreetName(name);
                    break;
                case "locality":
                    address.setCityName(name);
                    break;
                case "street_number":
                    address.setHouseNumber(Integer.parseInt(name));
                    String confirmationLevel = component.getConfirmationLevel();
                    if (!confirmationLevel.equalsIgnoreCase("confirmed")) {
                        String confirmationText
                                = "Double check the house number and street name, "
                                + "Google could not find this exact address";
                        model.addAttribute("houseNumberConfirmation",
                                confirmationText);
                    }
                    break;
            }
        }
        model.addAttribute("formattedAddress", formattedAddress);
    }
}