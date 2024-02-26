package com.example.myapp.rest.restController;

import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherRestController {
    //delete real OpenWeatherMap.org key before commit
    private static final String API_KEY =
//            "here was my real OpenWeatherMap.org Api Key";
            "weatherApiKey";

    private final String weatherApiUrl;

    public WeatherRestController(@Value("${weather.api.url}")
                                 String weatherApiUrl) {
        this.weatherApiUrl = weatherApiUrl;
    }

    @GetMapping("/getWeather/lat={latitude}&lon={longitude}")
    public WeatherResponse getWeather(@PathVariable("latitude") double latitude,
                                      @PathVariable("longitude") double longitude) {
        ResponseEntity<WeatherResponse> responseEntity =
                new RestTemplate().getForEntity(weatherApiUrl
                        + "/data/2.5/weather?lat=" + latitude + "&lon="
                        + longitude + "&appid=" + API_KEY
                        + "&units=metric", WeatherResponse.class);

        return responseEntity.getBody();
    }
}