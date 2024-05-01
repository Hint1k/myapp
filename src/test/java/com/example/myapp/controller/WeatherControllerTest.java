package com.example.myapp.controller;

import com.example.myapp.rest.restController.LocationRestController;
import com.example.myapp.rest.weatherJsonParsing.Main;
import com.example.myapp.rest.weatherJsonParsing.WeatherResponse;
import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of the controller layer
@WebMvcTest(WeatherController.class)
@WithMockUser // spring security requires a user with a password
public class WeatherControllerTest {

    @MockBean
    private CourierService courierService;

    @MockBean
    private LocationRestController locationRestController;

    @Autowired
    private MockMvc mockMvc;

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
    public void testGetHtmlWeather() {
        // Mocking the response from WeatherRestController
        WeatherResponse weatherResponse = new WeatherResponse();
        Main main = new Main();
        main.setTemperature(20.0);
        main.setFeelsLike(18.0);
        weatherResponse.setMain(main);
        Mockito.when(locationRestController.getWeather(Mockito.anyString()))
                .thenReturn(weatherResponse);

        // testing
        try {
            mockMvc.perform(post("/api/weather/weather-html")
                            .with(csrf())
                            .param("address", "Moscow")
                            .contentType(MediaType.TEXT_HTML_VALUE)
                    )
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("address", "Moscow"))
                    .andExpect(model().attribute("temperature", 20.0))
                    .andExpect(model().attribute("feelsLike", 18.0))
                    .andExpect(view().name("weather-report"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetHtmlWeather() fails");
            throw new RuntimeException(e);
        }

        Mockito.verify(locationRestController, Mockito.times(1))
                .getWeather("Moscow");
    }
}