package com.example.myapp.service;

import com.example.myapp.dao.AddressDaoImpl;
import com.example.myapp.testData.AddressData;
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
    private AddressServiceImpl addressServiceImpl;

    @Mock
    private AddressDaoImpl addressDaoImpl;

    private static List<Address> addresses;

    @BeforeAll
    public static void createAddressList() {
        AddressData addressData = new AddressData();
        addresses = addressData.getAddressData();
    }

    @Test
    public void testGetAddresses() {
        when(addressDaoImpl.getAddresses()).thenReturn(addresses);

        //testing
        List<Address> addressList = addressServiceImpl.getAddresses();

        assertEquals(6, addressList.size());
        assertEquals("11", addressList.get(0).getHouseNumber());
        assertEquals("Ivanov", addressList.get(0).getCourier().getLastName());

        verify(addressDaoImpl, times(1)).getAddresses();
    }

    @Test
    public void testGetAddress() {
        int id = 0;
        Address address = addresses.get(id);

        when(addressDaoImpl.getAddress(id)).thenReturn(address);

        //testing
        Address addressTest = addressServiceImpl.getAddress(id);

        assertEquals("Main street", addressTest.getStreetName());
        assertEquals("11", addressTest.getHouseNumber());
        assertEquals("Ivan", addressTest.getCourier().getFirstName());

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