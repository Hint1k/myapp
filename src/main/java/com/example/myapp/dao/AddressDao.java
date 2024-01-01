package com.example.myapp.dao;

import com.example.myapp.entity.Address;

import java.util.List;

public interface AddressDao {
    List<Address> getAddresses();
    Address getAddress(Integer id);
    void saveAddress(Address address);
    void deleteAddress(Integer id);
}
