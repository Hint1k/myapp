package com.example.myapp.restController;

import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest does not work with testGetWeather() due to wrong port error
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LocationRestControllerTest {

    @MockBean
    private CourierService courierService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.standaloneSetup(new LocationRestControllerTest()).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetLocation() {
        String urlString = "/getLocation?address=Moscow";
        String latitudeJson = "$.results[0].geometry.location.lat";
        String longitudeJson = "$.results[0].geometry.location.lng";
        String latitude = "55.755826";
        String longitude = "37.6173";

        // testing
        try {
            mockMvc.perform(get(urlString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(latitudeJson).value(latitude))
                    .andExpect(jsonPath(longitudeJson).value(longitude))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetLocation() fails");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetWeather() {
        String urlString = "/getWeather?address=moscow";
        String latitudeJson = "$.coord.lat";
        String longitudeJson = "$.coord.lon";
        double latitude = 55.7558;
        double longitude = 37.6172;

        // testing
        try {
            mockMvc.perform(get(urlString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(latitudeJson).value(latitude))
                    .andExpect(jsonPath(longitudeJson).value(longitude))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetWeather() fails");
            throw new RuntimeException(e);
        }
    }
}