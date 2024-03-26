package com.example.myapp.controller;

import com.example.myapp.MyTestContext;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of controller layer
public class WeatherControllerTest extends MyTestContext{

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
    public void testGetJsonWeather() {
        String address = "moscow";
        String urlString = "/weather?address=" + address;

        // stubbing with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type",
                                "application/json")
                ));

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
        String address = "moscow";

        String urlString1 = "/maps/api/geocode/json?key="
                + GOOGLE_API_KEY + "&address=" + address;

        // stubbing location with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString1))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("location.json")
                ));

        String urlString2 = "/data/2.5/weather?lat=55.75&lon=37.59&appid="
                + WEATHER_API_KEY + "&units=metric";

        // stubbing weather with WireMock
        wireMockExtension.stubFor(WireMock.get(urlEqualTo(urlString2))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("weather.json")
                ));

        // testing the response for two stubs simultaneously
        try {
            mockMvc.perform(post("/api/weather/weather-html")
                            .with(csrf())
                            .param("address", address)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(address)
                    )
                    .andExpect(status().isOk())
                    .andExpect(view().name("weather-report"))
                    .andExpect(model().attributeExists("temperature"))
                    .andExpect(model().attributeExists("feelsLike"))
                    .andExpect(model().attributeExists("address"))
                    .andDo(print());
        } catch (Exception e) {
            System.out.println("testGetHtmlWeather() fails");
            throw new RuntimeException(e);
        }
    }
}