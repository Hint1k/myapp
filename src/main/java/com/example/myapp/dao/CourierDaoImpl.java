package com.example.myapp.dao;

import com.example.myapp.entity.Courier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourierDaoImpl implements CourierDao {

    private EntityManager entityManager;

    public CourierDaoImpl() {
    }

    @Autowired
    public CourierDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Courier> getCouriers() {
        Query query = entityManager.createQuery("from Courier");
        List<Courier> couriers = query.getResultList();
        return couriers;
    }

    @Override
    public Courier getCourier(Integer id) {
        Courier courier = entityManager.find(Courier.class, id);
        return courier;
    }

    @Override
    public void saveCourier(Courier courier) {
        //imitating "save" or "update" the record in the database
        if (courier.getId() == null) {
            entityManager.persist(courier);
        } else {
            entityManager.merge(courier);
        }
    }

    @Override
    public void deleteCourier(Integer id) {
        Query query = entityManager.createQuery("delete from Courier where id=:courierId");
        query.setParameter("courierId", id);
        query.executeUpdate();
    }
}
