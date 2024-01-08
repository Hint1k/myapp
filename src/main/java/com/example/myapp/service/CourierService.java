package com.example.myapp.service;

import com.example.myapp.entity.Courier;

import java.util.List;

public interface CourierService {
    List<Courier> getCouriers();
    Courier getCourier(Integer id);
    void saveCourier(Courier courier);
    void deleteCourier(Integer id);
}