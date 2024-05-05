package com.example.myapp.rest.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.validationJsonParsing.request.RequestAddress;
import com.example.myapp.rest.validationJsonParsing.request.ValidationRequest;
import com.example.myapp.rest.validationJsonParsing.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

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

    // cutting off the spaces entered by user to avoid validation errors
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/customers/order")
    public String validateAddress(@ModelAttribute("address") Address address,
                                  Model model, BindingResult bindingResult) {

        if (!validateAddressFields(address, bindingResult)) {
            return "order-form";
        }

        ValidationRequest validationRequest = createValidationRequest(address);
        ResponseEntity<String> response = sendValidationRequest(validationRequest);

        // if country code is unsupported google response has an error message
        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            if (response != null) { // handle unsupported region error
                String errorMessage = response.getBody();
                model.addAttribute("unsupportedRegion", errorMessage);
                return "order-form";
            } // handle other errors with no error message
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

    private boolean validateAddressFields(Address address,
                                          BindingResult bindingResult) {

        String countryName = address.getCountryName();
        String cityName = address.getCityName();
        String streetName = address.getStreetName();

        if (countryName == null || countryName.length() != 2) {
            bindingResult.rejectValue("countryName",
                    "countryCode.length",
                    "Country code must be exactly two letters.");
            return false;
        }

        if (cityName == null || cityName.isEmpty()) {
            bindingResult.rejectValue("cityName",
                    "componentNull",
                    "Empty fields are not allowed.");
            return false;
        }

        if (streetName == null || streetName.isEmpty()) {
            bindingResult.rejectValue("streetName",
                    "componentNull",
                    "Empty fields are not allowed.");
            return false;
        }

        String houseNumber = Objects.toString(address.getHouseNumber(), "");
        if (houseNumber == null || houseNumber.isEmpty()) {
            bindingResult.rejectValue("houseNumber",
                    "componentNull",
                    "Empty fields are not allowed.");
            return false;
        }

        return true;
    }

    private ValidationRequest createValidationRequest(Address address) {
        // a house number should always be before a street name
        String addressLines = address.getHouseNumber() + " "
                + address.getStreetName();
        // regionCode is google's name for "country name"
        String regionCode = address.getCountryName();
        // locality or postal_town is google's name for "city name"
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
        two letters country code, but it is an unsupported country code  */
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
        List<AddressComponent> addressComponents = responseAddress.getAddressComponents();

        if (!checkAllAddressComponentsPresent(addressComponents)
                || !checkConfirmationLevels(addressComponents)) {
            String confirmationText = "Double check the info you entered, "
                    + "Google could not find this exact address";
            model.addAttribute("errorConfirmation", confirmationText);
            return;
        }

        updateAddressAndModelAttributes(addressComponents, address,
                model, formattedAddress);
    }

    private boolean checkAllAddressComponentsPresent(
            List<AddressComponent> addressComponents) {

        boolean isStreetMissing = true;
        boolean isHouseMissing = true;
        boolean isCountryNameMissing = true;
        boolean isCityMissing = true;

        for (AddressComponent component : addressComponents) {
            if (component.getComponentType().equals("country")) {
                isCountryNameMissing = false;
            }
            if (component.getComponentType().equals("locality")
                    || component.getComponentType().equals("postal_town")) {
                isCityMissing = false;
            }
            if (component.getComponentType().equals("route")) {
                isStreetMissing = false;
            }
            if (component.getComponentType().equals("street_number")) {
                isHouseMissing = false;
            }
        }

        return !isHouseMissing && !isStreetMissing
                && !isCityMissing && !isCountryNameMissing;
    }

    private boolean checkConfirmationLevels(List<AddressComponent> addressComponents) {
        for (AddressComponent component : addressComponents) {
            String confirmationLevel = component.getConfirmationLevel();
            if (!confirmationLevel.equalsIgnoreCase("confirmed")) {
                return false;
            }
        }
        return true;
    }

    private void updateAddressAndModelAttributes(
            List<AddressComponent> addressComponents, Address address,
            Model model, String formattedAddress) {

        for (AddressComponent component : addressComponents) {
            String name = component.getComponentName().getText();
            switch (component.getComponentType()) {
                case "country":
                    address.setCountryName(name);
                    break;
                case "locality":
                case "postal_town":
                    address.setCityName(name);
                    break;
                case "route":
                    address.setStreetName(name);
                    break;
                case "street_number":
                    address.setHouseNumber(Integer.parseInt(name));
                    break;
            }
        }
        model.addAttribute("formattedAddress", formattedAddress);
    }
}