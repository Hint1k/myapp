package com.example.myapp.restController;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// unit test of controller layer
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LocationRestControllerTest {

    private MockMvc mockMvc;

    private static final String GOOGLE_API_KEY = "googleApiKey";

    private static final String WEATHER_API_KEY = "weatherApiKey";

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension
            .newInstance()
            .options(wireMockConfig().dynamicPort()
                    .usingFilesUnderClasspath("wiremock"))
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("google.api.url", wireMockExtension::baseUrl);
        registry.add("weather.api.url", wireMockExtension::baseUrl);
        registry.add("google.api.key", () -> GOOGLE_API_KEY);
        registry.add("weather.api.key", () -> WEATHER_API_KEY);
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetLocation() {
        String urlString = "/maps/api/geocode/json?key=" +
                GOOGLE_API_KEY + "&address=moscow";

        // stubbing with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("location.json")));

        // parsing the response in json format
        String latitude = "$.results[0].geometry.location.lat";
        String longitude = "$.results[0].geometry.location.lng";

        // testing
        try {
            mockMvc.perform(get("/location?address=moscow"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(latitude).value(55.75))
                    .andExpect(jsonPath(longitude).value(37.59))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetLocation() fails");
            throw new RuntimeException(e);
        }

        wireMockExtension.verify(exactly(1), getRequestedFor(urlEqualTo(urlString)));
    }

    @Test
    public void testGetWeather() {
        String urlString1 = "/maps/api/geocode/json?key="
                + GOOGLE_API_KEY + "&address=moscow";

        // stubbing location with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("location.json")));

        String urlString2 = "/data/2.5/weather?lat=55.75&lon=37.59&appid="
                + WEATHER_API_KEY + "&units=metric";

        // stubbing weather with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("weather.json")));

        // parsing the response in json format
        String latitude = "$.coord.lat";
        String longitude = "$.coord.lon";

        // testing the response for two stubs simultaneously
        try {
            mockMvc.perform(get("/weather?address=moscow"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(latitude).value(55.75))
                    .andExpect(jsonPath(longitude).value(37.59))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetWeather() fails");
            throw new RuntimeException(e);
        }

        wireMockExtension.verify(exactly(1), getRequestedFor(urlEqualTo(urlString1)));
        wireMockExtension.verify(exactly(1), getRequestedFor(urlEqualTo(urlString2)));
    }
}