package com.example.myapp.converter;

import com.example.myapp.entity.Courier;
import com.example.myapp.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// needed this class to avoid conversion error
@Component
public class StringToCourier implements Converter<String, Courier> {

    private CourierService courierService;

    @Autowired
    public StringToCourier(CourierService courierService) {
        this.courierService = courierService;
    }

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
