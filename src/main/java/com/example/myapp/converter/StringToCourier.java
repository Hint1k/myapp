package com.example.myapp.converter;

import com.example.myapp.entity.Courier;
import com.example.myapp.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// I needed this class to avoid conversion error
@Component
public class StringToCourier implements Converter<String, Courier> {

    @Autowired
    private CourierService courierService;

    public StringToCourier() {}

    @Override
    public Courier convert(String string) {
         if (string.isEmpty()) {
            return  null;
         }  else {
             Integer id = Integer.valueOf(string);
             return courierService.getCourier(id);
         }
    }
}