package com.example.myapp.service;

import com.example.myapp.dao.CustomerDao;
import com.example.myapp.entity.Customer;
import com.example.myapp.entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;

    private PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public void saveCustomer(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        String password = "{bcrypt}" + encodedPassword;
        customer.setPassword(password);
        customer.setIsEnabled(1);
        String username = customer.getUsername();

        Role role = new Role();
        role.setCustomer(customer);
        role.setUsername(username);
        role.setAuthority("ROLE_CUSTOMER");
        customer.setRole(role);

        customerDao.saveCustomer(customer);
    }
}