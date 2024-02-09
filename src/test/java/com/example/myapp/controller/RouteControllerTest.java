package com.example.myapp.controller;

import com.example.myapp.testData.MapData;
import com.example.myapp.entity.Address;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;
import com.example.myapp.service.AddressService;
import com.example.myapp.service.CourierService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
/* spring security requires a user with a password,
also need to set a specific "user1" with digits in the username
because of Principal attribute and how the controller works */
@WithMockUser(username = "user1")
public class RouteControllerTest {

    @MockBean
    private AddressService addressService;

    // requires for tests to work
    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private Map<Address, Coordinates> map;

    private static Map<Address, Coordinates> mapStatic;

    private List<Address> addresses;

    private static List<Address> addressesStatic;

    @BeforeAll
    public static void createData() {
        MapData mapData = new MapData();
        mapStatic = mapData.getMap();
        addressesStatic = mapData.getAddresses();
    }

    @BeforeEach
    public void setupData() {
        // in case of any changes
        addresses = addressesStatic;
        map = mapStatic;
    }

    @Test
    public void testGetAddresses() {
        when(addressService.getAddresses()).thenReturn(addresses);

        // testing
        try {
            mockMvc.perform(get("/courier/getAddresses"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("addresses", hasSize(6)))
                    .andExpect(model().attribute("addresses", hasItem(allOf(
                            hasProperty("streetName", equalTo("Main street")),
                            hasProperty("houseNumber", equalTo(10)),
                            hasProperty("countryName", equalTo("Russia"))
                    ))))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetAddresses() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testGetCoordinates() {
        when(addressService.getAddresses()).thenReturn(addresses);
        testGetAddresses();

        // testing
        try {
            mockMvc.perform(get("/courier/getCoordinates"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("addresses", hasSize(6)))
                    .andExpect(model().attribute("map", aMapWithSize(6)))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetCoordinates() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testCalculateRoute() {
        when(addressService.getAddresses()).thenReturn(addresses);
        testGetCoordinates();

        // testing
        try {
            mockMvc.perform(get("/courier/getRoute"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("route", hasSize(7)))
                    .andExpect(model().attribute("distance", equalTo("7839 km")))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testCalculateRoute() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }
}