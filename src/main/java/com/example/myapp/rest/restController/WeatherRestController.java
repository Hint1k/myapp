package com.example.myapp.rest.restController;

import com.example.myapp.exception.InvalidWeatherResponse;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherRestController {

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final String weatherApiUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(WeatherRestController.class);

    public WeatherRestController(@Value("${weather.api.url}")
                                 String weatherApiUrl) {
        this.weatherApiUrl = weatherApiUrl;
    }

    @GetMapping("/weather/lat={latitude}&lon={longitude}")
    public WeatherResponse getWeather(@PathVariable("latitude") double latitude,
                                      @PathVariable("longitude") double longitude) {
        try {
            ResponseEntity<WeatherResponse> responseEntity =
                    new RestTemplate().getForEntity(weatherApiUrl
                            + "/data/2.5/weather?lat=" + latitude + "&lon="
                            + longitude + "&appid=" + weatherApiKey
                            + "&units=metric", WeatherResponse.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            logger.error("OpenWeatherMap api response error: {}", e.getMessage(), e);
            throw new InvalidWeatherResponse("Invalid Weather response");
        }
    }
}