package com.example.myapp.converter;

import com.example.myapp.entity.Courier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// did not have any errors and wrote this class just for learning purposes
@Component
public class CourierToString implements Converter<Courier, String> {

    @Override
    public String convert(Courier courier) {
        String string;
        if (courier.getId() == null) {
            string = null;
        } else {
            string = String.valueOf(courier.getId());
        }
        return string;
    }
}