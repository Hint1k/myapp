package com.example.myapp.rest.restController;

import com.example.myapp.exception.InvalidGoogleResponse;
import com.example.myapp.rest.locationJsonParsing.LocationResponse;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LocationRestController {

    private final String googleApiKey;

    private final String googleApiUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(LocationRestController.class);

    public LocationRestController(@Value("${google.api.url}")
                                  String googleApiUrl,
                                  @Value("${google.api.key}")
                                  String googleApiKey) {
        this.googleApiUrl = googleApiUrl;
        this.googleApiKey = googleApiKey;
    }

    @GetMapping("/location")
    public LocationResponse getLocation(@RequestParam String address) {

        try {
            ResponseEntity<LocationResponse> locationResponse =
                    new RestTemplate().getForEntity(googleApiUrl
                            + "/maps/api/geocode/json?key="
                            + googleApiKey + "&address="
                            + address, LocationResponse.class);

            return locationResponse.getBody();
        } catch (Exception e) {
            logger.error("Google api response error: {}", e.getMessage(), e);
            throw new InvalidGoogleResponse("Invalid Google response");
        }
    }

    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam String address) {

        LocationResponse locationResponse = getLocation(address);

        double latitude = locationResponse.getResults()[0]
                .getGeometry().getLocation().getLatitude();
        double longitude = locationResponse.getResults()[0]
                .getGeometry().getLocation().getLongitude();

        String url = "http://localhost:8080" + "/weather/lat="
                + latitude + "&lon=" + longitude;

        ResponseEntity<WeatherResponse> weatherResponse =
                new RestTemplate().getForEntity(url,
                        WeatherResponse.class);

        return weatherResponse.getBody();
    }
}