package com.example.myapp.service;

import com.example.myapp.dao.CourierDaoImpl;
import com.example.myapp.testData.CourierData;
import com.example.myapp.entity.Courier;
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
public class CourierServiceImplTest {

    @InjectMocks
    private CourierServiceImpl courierServiceImpl;

    @Mock
    private CourierDaoImpl courierDaoImpl;

    private static List<Courier> couriers;

    @BeforeAll
    public static void createCourierList() {
        CourierData courierData = new CourierData();
        couriers = courierData.getCourierData();
    }

    @Test
    public void testGetCourier() {
        int id = 2;
        Courier courier = couriers.get(id);

        // testing
        courierServiceImpl.getCourier(id);

        assertEquals("Jack", courier.getFirstName());
        assertEquals("Black", courier.getLastName());
        assertEquals(3, courier.getId());

        verify(courierDaoImpl, times(1)).getCourier(id);
    }

    @Test
    public void testGetCouriers() {
        when(courierDaoImpl.getCouriers()).thenReturn(couriers);

        // testing
        List<Courier> courierList = courierServiceImpl.getCouriers();

        assertEquals(5, courierList.size());
        assertEquals("Black", courierList.get(2).getLastName());
        assertEquals("Jane", courierList.get(1).getFirstName());

        verify(courierDaoImpl, times(1)).getCouriers();
    }

    @Test
    public void testSaveCourier() {
        int id = 0;
        Courier courier = couriers.get(id);

        doNothing().when(courierDaoImpl).saveCourier(courier);

        // testing
        courierServiceImpl.saveCourier(courier);

        verify(courierDaoImpl, times(1)).saveCourier(courier);
    }

    @Test
    public void testDeleteCourier() {
        Courier courier = couriers.get(0);
        int id = courier.getId();

        doNothing().when(courierDaoImpl).deleteCourier(id);

        // testing
        courierServiceImpl.deleteCourier(id);

        verify(courierDaoImpl, times(1)).deleteCourier(id);
    }
}