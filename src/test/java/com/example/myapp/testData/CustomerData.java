package com.example.myapp.testData;

import com.example.myapp.entity.Customer;
import com.example.myapp.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class CustomerData {

    private List<Customer> customers;

    private void createCustomerList() {

        customers = new ArrayList<>();

        Role role1 = new Role(
                11, "customer1", "ROLE_CUSTOMER",
                new Customer());
        Role role2 = new Role(
                12, "customer2", "ROLE_CUSTOMER",
                new Customer());
        Role role3 = new Role(
                13, "customer3", "ROLE_CUSTOMER",
                new Customer());
        Role role4 = new Role(
                14, "customer4", "ROLE_CUSTOMER",
                new Customer());
        Role role5 = new Role(
                15, "customer5", "ROLE_CUSTOMER",
                new Customer());

        Customer customer1 = new Customer(
                11, "Ivan", "Ivanov", "ivan@mail.com",
                "customer1", "123", 1, role1);
        Customer customer2 = new Customer(
                12, "Petr", "Petrov", "petr@mail.com",
                "customer2", "123", 1, role2);
        Customer customer3 = new Customer(
                13, "Sidor", "Sidorov", "sidor@mail.com",
                "customer3", "123", 1, role3);
        Customer customer4 = new Customer(
                14, "Oleg", "Olegov", "oleg@mail.com",
                "customer4", "123", 1, role4);
        Customer customer5 = new Customer(
                15, "Igor", "Igorev", "igor@mail.com",
                "customer5", "123", 1, role5);

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);
        customers.add(customer5);
    }

    public CustomerData() {
        createCustomerList();
    }

    public List<Customer> getCustomerData() {
        return customers;
    }

    public void setCustomerData(List<Customer> customers) {
        this.customers = customers;
    }
}