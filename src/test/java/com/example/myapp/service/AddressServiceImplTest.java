package com.example.myapp.service;

import com.example.myapp.dao.AddressDaoImpl;
import com.example.myapp.data.AddressData;
import com.example.myapp.entity.Address;
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
public class AddressServiceImplTest {

    @InjectMocks
    AddressServiceImpl addressServiceImpl;

    @Mock
    AddressDaoImpl addressDaoImpl;

    private static List<Address> addresses;

    @BeforeAll
    public static void createAddressList() {
        AddressData addressData = new AddressData();
        addresses = addressData.getAddressData();
    }

    @Test
    public void testGetAddress() {
        when(addressDaoImpl.getAddresses()).thenReturn(addresses);

        //testing
        List<Address> addressList = addressServiceImpl.getAddresses();

        assertEquals(5, addressList.size());
        assertEquals(10, addressList.get(0).getFlatNumber());
        assertEquals("Doe", addressList.get(0).getCourier().getLastName());

        verify(addressDaoImpl, times(1)).getAddresses();
    }

    @Test
    public void testGetAddresses() {
        int id = 0;
        Address address = addresses.get(id);

        when(addressDaoImpl.getAddress(id)).thenReturn(address);

        //testing
        Address addressTest = addressServiceImpl.getAddress(id);

        assertEquals("Main", addressTest.getStreetName());
        assertEquals(1, addressTest.getHouseNumber());
        assertEquals("John", addressTest.getCourier().getFirstName());

        verify(addressDaoImpl, times(1)).getAddress(id);
    }

    @Test
    public void testSaveAddress() {
        int id = 0;
        Address address = addresses.get(id);

        doNothing().when(addressDaoImpl).saveAddress(address);

        // testing
        addressServiceImpl.saveAddress(address);

        verify(addressDaoImpl, times(1)).saveAddress(address);
    }

    @Test
    public void testDeleteAddress() {
        Address address = addresses.get(0);
        int id = address.getId();

        doNothing().when(addressDaoImpl).deleteAddress(id);

        // testing
        addressServiceImpl.deleteAddress(id);

        verify(addressDaoImpl, times(1)).deleteAddress(id);
    }
}