package com.example.myapp.controller;

import com.example.myapp.data.CourierData;
import com.example.myapp.entity.Courier;
import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourierController.class)
@WithMockUser // spring security requires a user with a password
public class CourierControllerTest {

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private static List<Courier> couriers;
    private static List<Courier> couriersStatic;

    @BeforeAll
    public static void createCourierList() {
        CourierData courierData = new CourierData();
        couriersStatic = courierData.getCourierData();
    }

    @Test
    public void testGetCourierList() {
        couriers = couriersStatic;

        Mockito.when(courierService.getCouriers()).thenReturn(couriers);

        try {
            mockMvc.perform(get("/courier/showList"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("courier-list"))
                    .andExpect(model().attribute("couriers", hasSize(5)))
                    .andExpect(model().attribute("couriers", hasItem(allOf(
                            hasProperty("firstName", equalTo("John")),
                            hasProperty("lastName", equalTo("Doe")),
                            hasProperty("phone", equalTo("+79991111111"))
                    ))));
        } catch (Exception e) {
            System.out.println("testGetCourierList() fails");
            throw new RuntimeException(e);
        }
    }

    // the rest is in the next commit
}
