package com.example.myapp.dao;

import com.example.myapp.entity.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressDaoImpl implements AddressDao {

    @Autowired
    EntityManager entityManager;

    public AddressDaoImpl() {
    }

    @Override
    public List<Address> getAddresses() {
        Query query = entityManager.createQuery("from Address");
        List<Address> addresses = query.getResultList();
        return addresses;
    }

    @Override
    public Address getAddress(Integer id) {
        Address address = entityManager.find(Address.class, id);
        return address;
    }

    @Override
    public void saveAddress(Address address) {
        //imitating "save" or "update" the record in the database
        if (address.getId() == null) {
            entityManager.persist(address);
        } else {
            entityManager.merge(address);
        }
    }

    @Override
    public void deleteAddress(Integer id) {
        Query query = entityManager
                .createQuery("delete from Address where id=:addressId");
        query.setParameter("addressId", id);
        query.executeUpdate();
    }
}