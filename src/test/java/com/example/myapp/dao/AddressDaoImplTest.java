package com.example.myapp.dao;

import com.example.myapp.data.AddressData;
import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// unit testing of dao layer
@DataJpaTest // disable full auto configuration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private List<Courier> couriers;

    private List<Address> addresses;

    private static List<Address> addressesStatic;

    @BeforeAll
    public static void createAddressList() {
        // creating courier and address data using Address constructor
        AddressData addressData = new AddressData();
        addressesStatic = addressData.getAddressData();
    }

    @Test
    @BeforeEach // need to re-new database for each method
    public void testMergeAddress() {
        addresses = addressesStatic;

        entityManager.merge(addresses.get(0));
        entityManager.merge(addresses.get(1));
        entityManager.merge(addresses.get(2));
        entityManager.merge(addresses.get(3));
        entityManager.merge(addresses.get(4));

        Query query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        Assertions.assertThat(addresses).extracting(Address::getStreetName)
                .contains("Sun", "Moon");
    }

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

        assertEquals("Back", address.getStreetName());
        assertEquals("Black", address.getCourier().getLastName());
    }

    @Test
    public void testPersistAddress() {
        Courier courier6 = new Courier(
                "Harry", "Potter", "+79991111166");
        Address address6 = new Address(
                "Mars", 6, 99, courier6);
        entityManager.persist(address6);

        Query query = entityManager.createQuery("from Address");
        addresses = query.getResultList();

        assertEquals(6, addresses.size());
        Assertions.assertThat(addresses).extracting(Address::getStreetName)
                .contains("Mars");
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
                .doesNotContain("Main");

        // testing that courier is not deleted together with address
        query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();
        assertEquals(5, couriers.size());
    }
}