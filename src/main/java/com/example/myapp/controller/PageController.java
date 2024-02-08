package com.example.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "index";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/manager")
    public String showManagerPage() {
        return "manager";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/weather/weather-location")
    public String showWeatherAndLocationPage() {
        return "weather-location";
    }

    @GetMapping("/weather/weather-report")
    public String showWeatherAndReportPage() {
        return "weather-report";
    }

    @GetMapping("/route")
    public String showRouteCalculationPage() {
        return "route";
    }
}