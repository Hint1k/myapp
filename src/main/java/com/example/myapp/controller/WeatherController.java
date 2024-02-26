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
@RequestMapping("/weather")
public class WeatherController {

    private final String googleApiUrl;

    public WeatherController(@Value("${google.api.url}")
                             String googleApiUrl) {
        this.googleApiUrl = googleApiUrl;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor
                = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // returns weather in JSON form
    @PostMapping("/weather-json")
    public String getJsonWeather(@RequestParam String cityName, Model model) {
        model.addAttribute("cityName", cityName);
        return "redirect:/getWeather?address=" + cityName;
    }

    // returns weather in HTML form
    @PostMapping("/weather-html")
    public String getHtmlWeather(@RequestParam String cityName, Model model) {
        LocationRestController locationRestController =
                new LocationRestController(googleApiUrl);
        WeatherResponse weatherResponse =
                locationRestController.getWeather(cityName);
        Main main = weatherResponse.getMain();
        double temperature = main.getTemperature();
        double feelsLike = main.getFeelsLike();

        model.addAttribute("temperature", temperature);
        model.addAttribute("feelsLike", feelsLike);
        model.addAttribute("cityName", cityName);

        return "weather-report";
    }
}