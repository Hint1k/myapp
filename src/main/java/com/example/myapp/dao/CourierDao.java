package com.example.myapp.dao;

import com.example.myapp.entity.Courier;

import java.util.List;

public interface CourierDao {

    List<Courier> getCouriers();

    Courier getCourier(Integer id);

    void saveCourier(Courier courier);

    void deleteCourier(Integer id);
}