package com.example.myapp.controller;

import com.example.myapp.entity.Customer;
import com.example.myapp.service.CourierService;
import com.example.myapp.service.CustomerService;
import com.example.myapp.testData.CustomerData;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of controller layer
@WebMvcTest(CustomerController.class)
@WithMockUser // spring security requires a user with a password
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    private List<Customer> customers;

    private static List<Customer> customerStatic;

    @BeforeAll
    public static void createCustomerList() {
        CustomerData customerData = new CustomerData();
        customerStatic = customerData.getCustomerData();
    }

    @BeforeEach
    public void setupCustomers() {
        // in case of any changes
        customers = customerStatic;
    }

    @Test
    public void testShowRegistrationForm() {
        // testing
        try {
            mockMvc.perform(get("/api/customers/customer-form"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("customer-form"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testShowRegistrationForm() fails");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = customers.get(0);
        doNothing().when(customerService).saveCustomer(customer);

        // testing
        try {
            mockMvc.perform(post("/api/customers")
                            .with(csrf())
                            .flashAttr("customer", customer)
                            .content(MediaType.TEXT_HTML_VALUE)
                            .content(new ObjectMapper().writeValueAsString(customer))
                    )
                    .andExpect(redirectedUrl("/registration-successful"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testSaveCustomer() fails");
            throw new RuntimeException(e);
        }

        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    public void testShowOrderFormPage() {
        //testing
        try {
            mockMvc.perform(get("/api/customers/order")) //order here used as noun
                    .andExpect(status().isOk())
                    .andExpect(view().name("order-form"))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testShowOrderFormPage() fails");
            throw new RuntimeException(e);
        }
    }
}