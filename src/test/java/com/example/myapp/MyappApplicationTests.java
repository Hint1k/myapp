package com.example.myapp;

import com.example.myapp.controller.AddressController;
import com.example.myapp.controller.CourierController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyappApplicationTests {

	@Autowired
	private CourierController courierController;

	@Autowired
	private AddressController addressController;

	//checking if the tests are working at all
	@Test
	public void contextLoads() {
		Assertions.assertNotNull(courierController);
		Assertions.assertNotNull(addressController);
	}
}