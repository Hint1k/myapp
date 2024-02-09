package com.example.myapp.controller;

import com.example.myapp.testData.AddressData;
import com.example.myapp.entity.Address;
import com.example.myapp.service.AddressService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

//unit test of controller layer
@WebMvcTest(AddressController.class)
@WithMockUser // spring security requires a user with a password
public class AddressControllerTest {

    @MockBean
    private AddressService addressService;

    // requires for tests to work
    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private List<Address> addresses;

    private static List<Address> addressesStatic;

    @BeforeAll
    public static void createAddressData() {
        AddressData addressData = new AddressData();
        addressesStatic = addressData.getAddressData();
    }

    @BeforeEach
    public void setupAddresses() {
        // in case of any changes
        addresses = addressesStatic;
    }

    @Test
    public void testGetAddressList() {
        when(addressService.getAddresses()).thenReturn(addresses);

        // testing
        try {
            mockMvc.perform(get("/address/showList"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("address-list"))
                    .andExpect(model().attribute("addresses", hasSize(6)))
                    .andExpect(model().attribute("addresses", hasItem(allOf(
                            hasProperty("streetName", equalTo("Main street")),
                            hasProperty("houseNumber", equalTo(10)),
                            hasProperty("countryName", equalTo("Russia"))
                    ))))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetAddressList() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testAddAddress() {
        // testing
        try {
            mockMvc.perform(get("/address/addAddress"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("address-form"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testAddAddress() fails");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSaveAddress() {
        Address address = addresses.get(0);
        doNothing().when(addressService).saveAddress(address);

        // testing
        try {
            mockMvc.perform(post("/address/saveAddress")
                            .with(csrf())
                            .flashAttr("address", address)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(address))
                    )
                    .andExpect(redirectedUrl("/address/showList"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testSaveAddress() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).saveAddress(address);
    }

    @Test
    public void testUpdateAddress() {
        Address address = addresses.get(0);
        int id = address.getId();
        id = id + 1;
        String urlString = "/address/updateAddress?addressId=" + id;

        when(addressService.getAddress(id)).thenReturn(address);

        // testing
        try {
            mockMvc.perform(get(urlString)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(address))
                    )
                    .andExpect(status().isOk())
                    .andExpect(view().name("address-form"))
                    .andDo(print())
                    .andReturn();

        } catch (Exception e) {
            System.out.println("testGetAddress() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddress(id);
    }

    @Test
    public void testDeleteAddress() {
        Address address = addresses.get(1);
        int id = address.getId();
        id = id + 1;
        String urlString = "/address/deleteAddress?addressId=" + id;

        doNothing().when(addressService).deleteAddress(id);

        // testing
        try {
            mockMvc.perform(get(urlString)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(address))
                    )
                    .andExpect(redirectedUrl("/address/showList"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testDeleteAddress() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).deleteAddress(id);
    }
}