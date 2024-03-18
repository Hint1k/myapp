package com.example.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/login-page")
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

    @GetMapping("/registration-successful")
    public String showRegisterPage() {
        return "registration-successful";
    }

    @GetMapping("/api/weather/weather-location")
    public String showWeatherAndLocationPage() {
        return "weather-location";
    }

    @GetMapping("/api/weather/weather-report")
    public String showWeatherAndReportPage() {
        return "weather-report";
    }

    @GetMapping("/api/courier/route")
    public String showRouteCalculationPage() {
        return "route";
    }

    @GetMapping("/upload")
    public String showFilesUploadPage() {
        return "upload-form";
    }
}