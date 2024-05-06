package com.example.myapp.dao;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* unit testing of dao layer using in-memory H2 database
and sql scripts located in schema.sgl and data.sql */
@DataJpaTest // disable full auto configuration
public class AddressDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private List<Address> addresses;

    @Test
    public void testGetAddresses() {
        Query query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        assertEquals(5, addresses.size());
        Assertions.assertThat(addresses).extracting(Address::getId)
                .containsOnly(1, 2, 3, 4, 5);
    }

    @Test
    public void testGetAddress() {
        int id = 3;
        Address address = entityManager.find(Address.class, id);

        assertEquals("Back street", address.getStreetName());
        assertEquals("Sidorov", address.getCourier().getLastName());
    }

    @Test
    public void testMergeAddress() {
        Courier courier6 = new Courier(
                6, "Harry", "Potter", "+79991111166");
        Address address6 = new Address(
                6, "Spain", "Madrid",
                "Forward street", "16", courier6);
        entityManager.merge(address6);

        Query query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        assertEquals(6, addresses.size());
        Assertions.assertThat(addresses).extracting(Address::getStreetName)
                .contains("Forward street");

        // testing that courier is also merged together with the address
        Assertions.assertThat(addresses).extracting(Address::getCourier)
                .extracting(Courier::getLastName)
                .contains("Potter");
    }

    @Test
    public void testPersistAddress() {
        Courier courier6 = new Courier(
                "Harry", "Potter", "+79991111166");
        Address address6 = new Address(
                "Spain", "Madrid",
                "Forward street", "16", courier6);
        entityManager.persist(address6);

        Query query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        assertEquals(6, addresses.size());
        Assertions.assertThat(addresses).extracting(Address::getStreetName)
                .contains("Forward street");
        Assertions.assertThat(addresses).extracting(Address::getCourier)
                .contains(courier6);
    }

    @Test
    public void testDeleteAddress() {
        int id = 1;
        Query query = entityManager.createQuery(
                "delete from Address where id=:addressId");
        query.setParameter("addressId", id);
        query.executeUpdate();

        query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        assertEquals(4, addresses.size());
        Assertions.assertThat(addresses).extracting(Address::getStreetName)
                .doesNotContain("Main street");

        // testing that courier is not deleted together with the address
        query = entityManager.createQuery("from Courier");
        List<Courier> couriers = query.getResultList();

        assertEquals(5, couriers.size());
    }
}