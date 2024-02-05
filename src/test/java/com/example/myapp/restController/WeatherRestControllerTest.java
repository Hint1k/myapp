package com.example.myapp.restController;

import com.example.myapp.rest.restController.WeatherRestController;
import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    MockMvc mockMvc;

    @Test
    public void testGetWeather() {
        String urlString = "/getWeather/lat=55.75&lon=37.61";
        String name = "$.name";
        String city = "Moscow";

        // testing
        try {
        mockMvc.perform(get(urlString))
                .andExpect(status().isOk())
                .andExpect(jsonPath(name).value(city))
                .andDo(print())
                .andReturn();
        } catch (Exception e) {
            System.out.println("testGetWeather() fails");
            throw new RuntimeException(e);
        }
    }
}