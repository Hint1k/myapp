package com.example.myapp.rest.restController;

import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherRestController {
    //delete real OpenWeatherMap.org key before commit
    private static final String API_KEY =
            "Here was My OpenWeatherMap.org API Key";

    @GetMapping("/getWeather/lat={latitude}&lon={longitude}")
    public WeatherResponse getWeather(@PathVariable("latitude") double latitude,
                                      @PathVariable("longitude") double longitude) {
        ResponseEntity<WeatherResponse> responseEntity =
                new RestTemplate().getForEntity(
                        "https://api.openweathermap.org/data/2.5/weather?lat="
                                + latitude + "&lon=" + longitude + "&appid="
                                + API_KEY + "&units=metric", WeatherResponse.class);

        return responseEntity.getBody();
    }
}