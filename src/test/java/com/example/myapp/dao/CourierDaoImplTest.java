package com.example.myapp.dao;

import com.example.myapp.data.CourierData;
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
public class CourierDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private static List<Courier> couriers;

    private static List<Courier> couriersStatic;

    @BeforeAll
    public static void createCourierList() {
        CourierData courierData = new CourierData();
        couriersStatic = courierData.getCourierData();
    }

    @Test
    @BeforeEach // need to re-new database for each method
    public void testMergeCourier() {
        couriers = couriersStatic;

        entityManager.merge(couriers.get(0));
        entityManager.merge(couriers.get(1));
        entityManager.merge(couriers.get(2));
        entityManager.merge(couriers.get(3));
        entityManager.merge(couriers.get(4));

        Query query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        Assertions.assertThat(couriers).extracting(Courier::getLastName)
                .contains("Doe", "Sue");
    }

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

        assertEquals("Jack", courier.getFirstName());
        assertEquals("Black", courier.getLastName());
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
        // id = 1..4 can't be deleted,
        // because they have foreign keys in real database
        int id = 5;
        Query query = entityManager.createQuery(
                "delete from Courier where id=:courierId");
        query.setParameter("courierId", id);
        query.executeUpdate();

        query = entityManager.createQuery("from Courier");
        couriers = query.getResultList();

        assertEquals(4, couriers.size());
        Assertions.assertThat(couriers).extracting(Courier::getFirstName)
                .doesNotContain("Martin");
    }
}