package com.example.myapp.controller;

import com.example.myapp.rest.restController.LocationRestController;
import com.example.myapp.rest.weatherJsonParsing.Main;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/weather")
public class WeatherController {

    private final String googleApiUrl;

    private final String googleApiKey;

    public WeatherController(@Value("${google.api.url}")
                             String googleApiUrl,
                             @Value("${google.api.key}")
                             String googleApiKey) {
        this.googleApiUrl = googleApiUrl;
        this.googleApiKey = googleApiKey;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // returns weather in JSON form
    @PostMapping("/weather-json")
    public String getJsonWeather(@RequestParam String address, Model model) {
        model.addAttribute("address", address);
        return "redirect:/weather?address=" + address;
    }

    // returns weather in HTML form
    @PostMapping("/weather-html")
    public String getHtmlWeather(@RequestParam String address, Model model) {
        LocationRestController locationRestController =
                new LocationRestController(googleApiUrl, googleApiKey);
        WeatherResponse weatherResponse =
                locationRestController.getWeather(address);
        Main main = weatherResponse.getMain();
        double temperature = main.getTemperature();
        double feelsLike = main.getFeelsLike();

        model.addAttribute("temperature", temperature);
        model.addAttribute("feelsLike", feelsLike);
        model.addAttribute("address", address);

        return "weather-report";
    }
}