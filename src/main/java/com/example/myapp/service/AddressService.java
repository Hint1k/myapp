package com.example.myapp.service;

import com.example.myapp.entity.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAddresses();

    Address getAddress(Integer id);

    void saveAddress(Address address);

    void deleteAddress(Integer id);
}