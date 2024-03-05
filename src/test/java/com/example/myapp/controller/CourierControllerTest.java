package com.example.myapp.controller;

import com.example.myapp.testData.CourierData;
import com.example.myapp.entity.Courier;
import com.example.myapp.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of controller layer
@WebMvcTest(CourierController.class)
@WithMockUser // spring security requires a user with a password
public class CourierControllerTest {

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private List<Courier> couriers;

    private static List<Courier> couriersStatic;

    @BeforeAll
    public static void createCourierList() {
        CourierData courierData = new CourierData();
        couriersStatic = courierData.getCourierData();
    }

    @BeforeEach
    public void setupCouriers() {
        // in case of any changes
        couriers = couriersStatic;
    }

    @Test
    public void testGetCourierList() {
        when(courierService.getCouriers()).thenReturn(couriers);

        // testing
        try {
            mockMvc.perform(get("/api/couriers"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("courier-list"))
                    .andExpect(model().attribute("couriers", hasSize(5)))
                    .andExpect(model().attribute("couriers", hasItem(allOf(
                            hasProperty("firstName", equalTo("Ivan")),
                            hasProperty("lastName", equalTo("Ivanov")),
                            hasProperty("phone", equalTo("+79991111111"))
                    ))))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetCourierList() fails");
            throw new RuntimeException(e);
        }

        verify(courierService, times(1)).getCouriers();
    }

    @Test
    public void testAddCourier() {
        // testing
        try {
            mockMvc.perform(get("/api/couriers/courier"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("courier-form"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testAddCourier() fails");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSaveCourier() {
        Courier courier = couriers.get(0);
        doNothing().when(courierService).saveCourier(courier);

        // testing
        try {
            mockMvc.perform(post("/api/couriers")
                            .with(csrf())
                            .flashAttr("courier", courier)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(courier))
                    )
                    .andExpect(redirectedUrl("/api/couriers"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testSaveCourier() fails");
            throw new RuntimeException(e);
        }

        verify(courierService, times(1)).saveCourier(courier);
    }

    @Test
    public void testUpdateCourier() {
        Courier courier = couriers.get(0);
        int id = courier.getId();
        String urlString = "/api/couriers/" + id;

        when(courierService.getCourier(id)).thenReturn(courier);

        // testing
        try {
            mockMvc.perform(put(urlString)
                            .with(csrf())
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(courier))
                    )
                    .andExpect(status().isOk())
                    .andExpect(view().name("courier-form"))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testGetCourier() fails");
            throw new RuntimeException(e);
        }

        verify(courierService, times(1)).getCourier(id);
    }

    @Test
    public void testDeleteCourier() {
        Courier courier = couriers.get(1);
        int id = courier.getId();
        String urlString = "/api/couriers/" + id;

        doNothing().when(courierService).deleteCourier(id);

        // testing
        try {
            mockMvc.perform(delete(urlString)
                            .with(csrf())
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(courier))
                    )
                    .andExpect(redirectedUrl("/api/couriers"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testDeleteCourier() fails");
            throw new RuntimeException(e);
        }

        verify(courierService, times(1)).deleteCourier(id);
    }
}