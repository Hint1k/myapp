package com.example.myapp.dao;

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
@DataJpaTest// disable full auto configuration
public class CourierDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private List<Courier> couriers;

    @Test
    public void testGetCouriers() {
        Query query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        assertEquals(5, couriers.size());
        Assertions.assertThat(couriers).extracting(Courier::getId)
                .containsOnly(1, 2, 3, 4, 5);
    }

    @Test
    public void tesGetCourier() {
        int id = 3;
        Courier courier = entityManager.find(Courier.class, id);

        assertEquals("Sidor", courier.getFirstName());
        assertEquals("Sidorov", courier.getLastName());
    }

    @Test
    public void testMergeCourier() {
        Courier courier6 = new Courier(
                5,"Frodo", "Baggings", "+79991111177");
        entityManager.merge(courier6);

        Query query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        assertEquals(5, couriers.size());
        Assertions.assertThat(couriers).extracting(Courier::getLastName)
                .contains("Baggings");
    }

    @Test
    public void testPersistCourier() {
        Courier courier6 = new Courier(
                "Harry", "Potter", "+79991111166");
        entityManager.persist(courier6);

        Query query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        assertEquals(6, couriers.size());
        Assertions.assertThat(couriers).extracting(Courier::getFirstName)
                .contains("Harry");
    }

    @Test
    public void testDeleteCourier() {
        /* a courier with id = 5 does not have a foreign key
        in the address table (see data.sql), so it can be deleted */
        int id = 5;
        Query query = entityManager.createQuery(
                "delete from Courier where id=:courierId");
        query.setParameter("courierId", id);
        query.executeUpdate();

        query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        assertEquals(4, couriers.size());
        Assertions.assertThat(couriers).extracting(Courier::getFirstName)
                .doesNotContain("Igor");
    }
}