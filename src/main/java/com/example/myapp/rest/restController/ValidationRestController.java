package com.example.myapp.rest.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.validationJsonParsing.MatchedSubstrings;
import com.example.myapp.rest.validationJsonParsing.Predictions;
import com.example.myapp.rest.validationJsonParsing.ValidationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class ValidationRestController {

    private final String googleApiKey;
    private final String googleApiUrl;
    private final ObjectMapper objectMapper;

    public ValidationRestController(@Value("${google.api.url}") String googleApiUrl,
                                    @Value("${google.api.key}") String googleApiKey,
                                    ObjectMapper objectMapper) {
        this.googleApiUrl = googleApiUrl;
        this.googleApiKey = googleApiKey;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/api/customers/order", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> validateAddress(@RequestParam String countryName,
                                                  @RequestParam String cityName,
                                                  @RequestParam String streetName,
                                                  @RequestParam String houseNumber) {
        try {
            // Encode the address components
            String encodedAddress = URLEncoder.encode(streetName + ", " + houseNumber + ", " + cityName + ", " + countryName, StandardCharsets.UTF_8);

            // Construct the API URL
            String apiUrl = googleApiUrl + "/maps/api/place/autocomplete/json?input=" + encodedAddress + "&key=" + googleApiKey;

            // Perform API call and get response
            String response = performApiCall(apiUrl);

            Address address = new Address(countryName, cityName, streetName, Integer.parseInt(houseNumber));

            // Check if the response contains valid results
            String validationResult = validateApiResponse(response, address);
            if (validationResult.equals("Address is valid")) {
                return ResponseEntity.ok(validationResult);
            } else {
                return ResponseEntity.badRequest().body(validationResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    private String performApiCall(String apiUrl) throws Exception {
        // Create URL object
        URL url = new URL(apiUrl);
        // Create connection object
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set request method
        connection.setRequestMethod("GET");
        // Get response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new RuntimeException("Failed to fetch response. Response code: " + responseCode);
        }
    }

    private String validateApiResponse(String response, Address address) throws Exception {
        ValidationResponse validationResponse = objectMapper.readValue(response, ValidationResponse.class);
        List<Predictions> predictions = validationResponse.getPredictions();

        // Check if predictions in json response is null
        if (predictions == null || predictions.isEmpty()) {
            return "No valid address is found";
        }

        Predictions prediction = predictions.get(0);
        List<MatchedSubstrings> matchedSubstrings = prediction.getMatchedSubstrings();

        // Check if matchedSubstrings in json response is null
        if (matchedSubstrings == null || matchedSubstrings.isEmpty()) {
            return "No valid address is found";
        }

        /* It defines the quantity of address components.
        In this case they are: country name, city name,
        street name and house number */
        int addressSize = 4;

        if (matchedSubstrings.size() < addressSize) {
            if (matchedSubstrings.size() == addressSize - 1) {
                return "One of the address components are incorrect";
            } else if (matchedSubstrings.size() == addressSize - 2) {
                return "Two of the address components are incorrect";
            } else { // matchedSubstring.size() == addressSize - 3
                return "Three of the address components are incorrect";
            }
        }

        String[] components = {address.getStreetName(), String.valueOf(address.getHouseNumber()), address.getCityName(), address.getCountryName()};
        String[] componentTypes = {"street", "houseNumber", "city", "country"};

        // Check which address component is missing
        for (int i = 0; i < components.length; i++) {
            if (!containsMatchedSubstring(matchedSubstrings, componentTypes[i], components[i], i)) {
                return componentTypes[i] + " is missing";
            }
        }

        // Address is considered valid if it passes all conditions
        return "Address is valid";
    }

    private boolean containsMatchedSubstring(List<MatchedSubstrings> matchedSubstrings, String componentType, String componentValue, int index) {
        // Get the matched substring corresponding to the component
        MatchedSubstrings substring = matchedSubstrings.get(index);

        // Get the expected length from the matched substring
        int expectedLength = substring.getLength();

        // Get the actual length of the componentValue
        int actualLength = componentValue.length();

        // Check if the actual length matches the expected length
        return actualLength == expectedLength;
    }
}