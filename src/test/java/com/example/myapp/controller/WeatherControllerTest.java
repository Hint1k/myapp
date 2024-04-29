package com.example.myapp.controller;

import com.example.myapp.rest.locationJsonParsing.Geometry;
import com.example.myapp.rest.locationJsonParsing.Location;
import com.example.myapp.rest.locationJsonParsing.LocationResponse;
import com.example.myapp.rest.locationJsonParsing.Result;
import com.example.myapp.rest.restController.LocationRestController;
import com.example.myapp.rest.restController.WeatherRestController;
import com.example.myapp.rest.weatherJsonParsing.Main;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
@WithMockUser // spring security requires a user with a password
public class WeatherControllerTest {

    @MockBean
    private CourierService courierService;

    @MockBean
    LocationRestController locationRestController;

    @MockBean
    WeatherRestController weatherRestController;

    @Autowired
    private MockMvc mockMvc;

    private final String googleApiUrl;

    private final String googleApiKey;

    public WeatherControllerTest(@Value("${google.api.url}")
                                 String googleApiUrl,
                                 @Value("${google.api.key}")
                                 String googleApiKey) {
        this.googleApiUrl = googleApiUrl;
        this.googleApiKey = googleApiKey;
    }

    @Test
    public void testGetJsonWeather() {
        String address = "moscow";
        String urlString = "/weather?address=" + address;

        // testing
        try {
            mockMvc.perform(post("/api/weather/weather-json")
                            .with(csrf())
                            .param("address", address)
                            .content(MediaType.APPLICATION_JSON_VALUE)
                            .content(address)
                    )
                    .andExpect(redirectedUrl(urlString))
                    .andExpect(header().string("Location", urlString))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetJsonWeather() fails");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetHtmlWeather() throws Exception {
        // Mock dependencies
        WeatherResponse weatherResponse = new WeatherResponse();
        Main main = new Main();
        main.setTemperature(20.0);
        main.setFeelsLike(18.0);
        weatherResponse.setMain(main);

        // Mock behavior of LocationRestController
        LocationResponse locationResponse = new LocationResponse();
        Result result = new Result();
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLatitude(55.755826); // Sample latitude for Moscow
        location.setLongitude(37.617300); // Sample longitude for Moscow
        geometry.setLocation(location);
        result.setGeometry(geometry);
        locationResponse.setResults(new Result[]{result});
        Mockito.when(locationRestController.getLocation(Mockito.anyString()))
                .thenReturn(locationResponse);

        // Mock behavior of WeatherRestController (optional,
        // if you want to test specific weather data)
        Mockito.when(weatherRestController.getWeather(Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(weatherResponse); // Comment out if not needed

        // Perform the request
        mockMvc.perform(post("/api/weather/weather-html")
                        .with(csrf())
                        .param("address", "Moscow")
                        .contentType(MediaType.TEXT_HTML_VALUE)
                        .content("Moscow"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("address", "Moscow"))
                .andExpect(model().attribute("temperature", 20.0))
                .andExpect(model().attribute("feelsLike", 18.0))
                .andExpect(view().name("weather-report"))
                .andDo(print())
                .andReturn();

        Mockito.verify(locationRestController, Mockito.times(1)).getLocation(Mockito.anyString());
    }
}