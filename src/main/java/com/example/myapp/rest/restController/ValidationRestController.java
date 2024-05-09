package com.example.myapp.rest.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.exception.*;
import com.example.myapp.rest.validationJsonParsing.request.RequestAddress;
import com.example.myapp.rest.validationJsonParsing.request.ValidationRequest;
import com.example.myapp.rest.validationJsonParsing.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
@RequestMapping("/api")
public class ValidationRestController {

    private final String googleApiKey;

    private final String googleApiUrl;

    private final ObjectMapper objectMapper;

    private static final Logger logger
            = LoggerFactory.getLogger(ValidationRestController.class);

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
        try {
            // preventing Google responses with HttpStatus != OK (with 2 exclusions)
            if (!isAllAddressFieldsFilledIn(address, bindingResult)) {
                return "order-form";
            }

            ValidationRequest validationRequest = createValidationRequest(address);
            ResponseEntity<String> response = sendValidationRequest(validationRequest);

            // handling 2 exclusions for Google responses
            if (response == null || response.getStatusCode() != HttpStatus.OK) {
                // Google response with unsupported region error
                if (response != null) {
                    String errorMessage = response.getBody();
                    model.addAttribute("unsupportedRegion", errorMessage);
                    return "order-form";
                } // Google response == null
                logger.error("Google null response. Request: {}", validationRequest);
                // this is info for user
                throw new InvalidGoogleResponse("Invalid Google response");
            }

            ValidationResponse validationResponse = objectMapper
                    .readValue(response.getBody(), ValidationResponse.class);
            handleValidationResponse(validationResponse, address, model);
            return "order-form";

        } catch (Exception e) {
            // handling other errors
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean isAllAddressFieldsFilledIn(Address address,
                                               BindingResult bindingResult) {

        String countryName = address.getCountryName();
        String cityName = address.getCityName();
        String streetName = address.getStreetName();
        String houseNumber = address.getHouseNumber();

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
            // the exceptions handled in validateAddress() method
            return null;
        }
    }

    private String toJsonString(Object object) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, object);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            // the exceptions handled in validateAddress() method
            return null;
        }
    }

    private ResponseEntity<String> handleBadRequestException(
            HttpClientErrorException.BadRequest ex) {

        /* Handling a response from Google when a customer correctly entered
        two letters country code, but it is an unsupported country code.
        Since it is a new Google service, some countries are not implemented yet. */
        try {
            ErrorResponse errorResponse = objectMapper.readValue(ex
                    .getResponseBodyAsString(), ErrorResponse.class);
            return ResponseEntity.badRequest().body(errorResponse.getError().getMessage());
        } catch (IOException e) {
            // the exceptions handled in validateAddress() method
            return null;
        }
    }

    private void handleValidationResponse(
            ValidationResponse validationResponse, Address address, Model model) {

        ValidationResult validationResult = validationResponse.getValidationResult();
        ResponseAddress responseAddress = validationResult.getResponseAddress();
        String formattedAddress = responseAddress.getFormattedAddress();
        List<AddressComponent> addressComponents = responseAddress.getAddressComponents();

        if (!isAllAddressComponentsPresent(addressComponents)
                || !isAllAddressComponentsConfirmed(addressComponents)) {
            String confirmationText = "Double check the info you entered, "
                    + "Google could not find this exact address";
            model.addAttribute("errorConfirmation", confirmationText);
            return;
        }

        updateAddressAndModelAttributes(addressComponents, address,
                model, formattedAddress);
    }

    private boolean isAllAddressComponentsPresent(
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

    private boolean isAllAddressComponentsConfirmed(
            List<AddressComponent> addressComponents) {

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
                case "locality": // this is a standard component name
                case "postal_town": // UK has a different component name (Google's mistake)
                    address.setCityName(name);
                    break;
                case "route":
                    address.setStreetName(name);
                    break;
                case "street_number":
                    address.setHouseNumber(name);
                    break;
            }
        }
        model.addAttribute("formattedAddress", formattedAddress);
        model.addAttribute("addressValidated", true);
    }
}