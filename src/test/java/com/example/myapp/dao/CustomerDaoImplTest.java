package com.example.myapp.dao;

import com.example.myapp.entity.Customer;
import com.example.myapp.entity.Role;
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
public class CustomerDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private List<Customer> customers;

    @Test
    public void testSaveCustomer() {
        Role role6 = new Role("customer2", "ROLE_CUSTOMER");

        Customer customer6 = new Customer(
                "Ivan", "Ivanov", "ivan@mail.com",
                "customer2", "123", 1, role6);

        entityManager.persist(customer6);

        Query query = entityManager.createQuery("from Customer");
        customers = query.getResultList();

        assertEquals(6, customers.size());
        Assertions.assertThat(customers).extracting(Customer::getFirstName)
                .contains("Ivan");
        Assertions.assertThat(customers).extracting(Customer::getRole)
                .contains(role6);
    }
}