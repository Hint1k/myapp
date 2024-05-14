package com.example.myapp.service;

import com.example.myapp.dao.CustomerDaoImpl;
import com.example.myapp.entity.Customer;
import com.example.myapp.testData.CustomerData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// unit testing of service layer
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Mock
    private CustomerDaoImpl customerDaoImpl;

    private static List<Customer> customers;

    @BeforeAll
    public static void createCustomerList() {
        CustomerData customerData = new CustomerData();
        customers = customerData.getCustomerData();
    }

    @Test
    public void testGetCustomers() {
        when(customerDaoImpl.getCustomers()).thenReturn(customers);

        // testing
        List<Customer> customerList = customerServiceImpl.getCustomers();

        assertEquals(5, customerList.size());
        assertEquals("Petr", customerList.get(1).getFirstName());
        assertEquals("Sidorov", customerList.get(2).getLastName());

        verify(customerDaoImpl, times(1)).getCustomers();
    }

    @Test
    public void testSaveCustomer() {
        int id = 1;
        Customer customer = customers.get(id);

        doNothing().when(customerDaoImpl).saveCustomer(customer);

        // testing
        customerServiceImpl.saveCustomer(customer);

        verify(customerDaoImpl, times(1)).saveCustomer(customer);
    }
}