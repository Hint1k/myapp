//package com.example.myapp.rest.restController;
//
//import com.example.myapp.entity.Address;
//import com.example.myapp.rest.validationJsonParsing.request.RequestAddress;
//import com.example.myapp.rest.validationJsonParsing.request.ValidationRequest;
//import com.example.myapp.rest.validationJsonParsing.response.ValidationResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@RequestMapping("/api")
//@RestController()
//public class ValidationRestController {
//
//    private final String googleApiKey;
//
//    private static final String GOOGLE_API_URL
//            = "https://addressvalidation.googleapis.com/v1:validateAddress?key=";
//
//    private final ObjectMapper objectMapper;
//
//    public ValidationRestController(@Value("${google.api.key}")
//                                    String googleApiKey,
//                                    ObjectMapper objectMapper) {
//        this.googleApiKey = googleApiKey;
//        this.objectMapper = objectMapper;
//    }
//
//    // Rename the endpoint to maybe customers/address-validation later
//    @PostMapping("/customers/order")
//    public ResponseEntity<String> validateAddress(@ModelAttribute
//                                                  Address address,
//                                                  @ModelAttribute("formattedAddress")
//                                                  String formattedAddress) {
//
//        String addressLines = address.getHouseNumber() + " " + address.getStreetName();
//        String regionCode = address.getCountryName();
//        String locality = address.getCityName();
//
//        ValidationRequest validationRequest = new ValidationRequest();
//        RequestAddress requestAddress = new RequestAddress();
//        requestAddress.setRegionCode(regionCode);
//        requestAddress.setLocality(locality);
//        requestAddress.setAddressLines(List.of(addressLines));
//        validationRequest.setRequestAddress((requestAddress));
//
//        String jsonString;
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            objectMapper.writeValue(outputStream, validationRequest);
//            jsonString = outputStream.toString(StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        String url = GOOGLE_API_URL + googleApiKey;
//        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
//        ResponseEntity<String> response = new RestTemplate().postForEntity(url, entity, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                ValidationResponse validationResponse = objectMapper.readValue(response.getBody(), ValidationResponse.class);
//                if (validationResponse.getValidationResult() != null && validationResponse.getValidationResult().getResponseAddress() != null
//                        && validationResponse.getValidationResult().getResponseAddress().getFormattedAddress() != null) {
//                    return new ResponseEntity<>("Address validated successfully: "
//                            + validationResponse.getValidationResult().getResponseAddress().getFormattedAddress(), HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<>("Error: 'formattedAddress' key not found in response", HttpStatus.BAD_REQUEST);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new ResponseEntity<>("Error parsing response: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//            }
//        } else {
//            return new ResponseEntity<>("Error validating address: " + response.getStatusCode(), HttpStatus.BAD_REQUEST);
//        }
//    }
//}
package com.example.myapp.rest.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.validationJsonParsing.request.RequestAddress;
import com.example.myapp.rest.validationJsonParsing.request.ValidationRequest;
import com.example.myapp.rest.validationJsonParsing.response.ResponseAddress;
import com.example.myapp.rest.validationJsonParsing.response.ValidationResponse;
import com.example.myapp.rest.validationJsonParsing.response.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ValidationRestController {

    private final String googleApiKey;
    private static final String GOOGLE_API_URL
            = "https://addressvalidation.googleapis.com/v1:validateAddress?key=";

    private final ObjectMapper objectMapper;

    public ValidationRestController(@Value("${google.api.key}")
                                    String googleApiKey,
                                    ObjectMapper objectMapper) {
        this.googleApiKey = googleApiKey;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/customers/order")
    public String validateAddress(@ModelAttribute Address address, Model model) {
        String addressLines = address.getHouseNumber() + " "
                + address.getStreetName();
        String regionCode = address.getCountryName();
        String locality = address.getCityName();

        ValidationRequest validationRequest = new ValidationRequest();
        RequestAddress requestAddress = new RequestAddress();
        requestAddress.setRegionCode(regionCode);
        requestAddress.setLocality(locality);
        requestAddress.setAddressLines(List.of(addressLines));
        validationRequest.setRequestAddress(requestAddress);

        String jsonString;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            objectMapper.writeValue(outputStream, validationRequest);
            jsonString = outputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if needed
            return "redirect:/error";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = GOOGLE_API_URL + googleApiKey;
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> response = new RestTemplate()
                .postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ValidationResponse validationResponse = objectMapper
                        .readValue(response.getBody(), ValidationResponse.class);
                ValidationResult validationResult = validationResponse.getValidationResult();
                ResponseAddress responseAddress = validationResult.getResponseAddress();
                String formattedAddress = responseAddress.getFormattedAddress();
                if (formattedAddress != null) {
                    model.addAttribute("formattedAddress", formattedAddress);
                    // Add other attributes to model if needed
                    return "order-form";
                } else {
                    // Handle error if formatted address not found
                    return "redirect:/error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception if needed
                return "redirect:/error";
            }
        } else {
            // Handle error if response status is not OK
            return "redirect:/error";
        }
    }
}