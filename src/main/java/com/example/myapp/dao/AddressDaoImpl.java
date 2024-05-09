package com.example.myapp.dao;

import com.example.myapp.entity.Address;
import com.example.myapp.exception.DatabaseConnectionError;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressDaoImpl implements AddressDao {

    @Autowired
    EntityManager entityManager;

    private static final Logger logger
            = LoggerFactory.getLogger(AddressDaoImpl.class);

    public AddressDaoImpl() {
    }

    @Override
    public List<Address> getAddresses() {
        try {
            Query query = entityManager.createQuery("from Address");
            List<Address> addresses = query.getResultList();
            return addresses;
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public Address getAddress(Integer id) {
        try {
            Address address = entityManager.find(Address.class, id);
            return address;
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public void saveAddress(Address address) {
        try {
            //imitating "save" or "update" the record in the database
            if (address.getId() == null) {
                entityManager.persist(address);
            } else {
                entityManager.merge(address);
            }
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }

    @Override
    public void deleteAddress(Integer id) {
        try {
            Query query = entityManager
                    .createQuery("delete from Address where id=:addressId");
            query.setParameter("addressId", id);
            query.executeUpdate();
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            throw new DatabaseConnectionError("Failed to connect to database");
        }
    }
}