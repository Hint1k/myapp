package com.example.myapp.dao;

import com.example.myapp.entity.Customer;
import com.example.myapp.exception.DatabaseConnectionError;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    private EntityManager entityManager;

    private static final Logger logger
            = LoggerFactory.getLogger(CustomerDaoImpl.class);

    @Override
    public void saveCustomer(Customer customer) {
        try {
            entityManager.persist(customer);
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }
}