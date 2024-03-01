package com.example.myapp.restController;

import com.example.myapp.rest.restController.WeatherRestController;
import com.example.myapp.service.CourierService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of rest controller
@WebMvcTest(WeatherRestController.class)
@WithMockUser // spring security requires a user with a password
public class WeatherRestControllerTest {

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private static final String WEATHER_API_KEY = "weatherApiKey";

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort()
                    .usingFilesUnderClasspath("wiremock"))
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("weather.api.url", wireMockExtension::baseUrl);
        registry.add("weather.api.key", wireMockExtension::baseUrl);
    }

    @Test
    public void testGetWeather() {
        String urlString = "/data/2.5/weather?lat=55.75&lon=37.59&appid="
                + WEATHER_API_KEY + "&units=metric";

        // stubbing with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("weather.json")));

        // parsing the response in json format
        String temperature = "$.main.temp";
        String feels_like = "$.main.feels_like";

        // testing
        try {
            mockMvc.perform(get("/weather/lat=55.75&lon=37.59"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(temperature).value(0.3))
                    .andExpect(jsonPath(feels_like).value(-4.83))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetWeather() fails");
            throw new RuntimeException(e);
        }
    }
}