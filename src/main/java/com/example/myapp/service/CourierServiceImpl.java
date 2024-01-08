package com.example.myapp.service;

import com.example.myapp.dao.CourierDao;
import com.example.myapp.entity.Courier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

    private CourierDao courierDao;

    public CourierServiceImpl (){}

    @Autowired
    public CourierServiceImpl (CourierDao courierDao) {
        this.courierDao = courierDao;
    }

    @Override
    @Transactional
    public List<Courier> getCouriers() {
        return courierDao.getCouriers();
    }

    @Override
    @Transactional
    public Courier getCourier(Integer id) {
        return courierDao.getCourier(id);
    }

    @Override
    @Transactional
    public void saveCourier(Courier courier) {
        courierDao.saveCourier(courier);
    }

    @Override
    @Transactional
    public void deleteCourier(Integer id) {
        courierDao.deleteCourier(id);
    }
}