package com.example.myapp.dao;

import com.example.myapp.entity.Courier;
import com.example.myapp.exception.DatabaseConnectionError;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourierDaoImpl implements CourierDao {

    @Autowired
    private EntityManager entityManager;

    private static final Logger logger
            = LoggerFactory.getLogger(CourierDaoImpl.class);

    public CourierDaoImpl() {
    }

    @Override
    public List<Courier> getCouriers() {
        try {
            Query query = entityManager.createQuery("from Courier");
            List<Courier> couriers = query.getResultList();
            return couriers;
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public Courier getCourier(Integer id) {
        try {
            Courier courier = entityManager.find(Courier.class, id);
            return courier;
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public void saveCourier(Courier courier) {
        try {
            //imitating "save" or "update" the record in the database
            if (courier.getId() == null) {
                entityManager.persist(courier);
            } else {
                entityManager.merge(courier);
            }
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public void deleteCourier(Integer id) {
        try {
            Query query = entityManager
                    .createQuery("delete from Courier where id=:courierId");
            query.setParameter("courierId", id);
            query.executeUpdate();
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }
}