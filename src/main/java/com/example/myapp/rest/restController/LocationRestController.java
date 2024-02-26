package com.example.myapp.rest.restController;

import com.example.myapp.rest.locationJsonParsing.LocationResponse;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class LocationRestController {

    //delete real Google API key before commit
    private static final String API_KEY =
//            "here was my real Google Api Key";
            "googleApiKey";

    private final String googleApiUrl;

    public LocationRestController(@Value("${google.api.url}")
                                  String googleApiUrl) {
        this.googleApiUrl = googleApiUrl;
    }

    @GetMapping("/getLocation")
    public LocationResponse getLocation(@RequestParam String address) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(googleApiUrl)
                .path("/maps/api/geocode/json")
                .queryParam("key", API_KEY)
                .queryParam("address", address)
                .build();

        ResponseEntity<LocationResponse> locationResponse =
                new RestTemplate().getForEntity(uriComponents
                        .toUriString(), LocationResponse.class);

        return locationResponse.getBody();
    }

    @GetMapping("/getWeather")
    public WeatherResponse getWeather(@RequestParam String address) {

        LocationResponse locationResponse = getLocation(address);

        double latitude = locationResponse.getResults()[0]
                .getGeometry().getLocation().getLatitude();
        double longitude = locationResponse.getResults()[0]
                .getGeometry().getLocation().getLongitude();

        String url = "http://localhost:8080/getWeather/lat="
                + latitude + "&lon=" + longitude;

        ResponseEntity<WeatherResponse> weatherResponse =
                new RestTemplate().getForEntity(url,
                        WeatherResponse.class);

        return weatherResponse.getBody();
    }
}