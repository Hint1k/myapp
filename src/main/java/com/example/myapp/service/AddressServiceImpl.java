package com.example.myapp.service;

import com.example.myapp.dao.AddressDao;
import com.example.myapp.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao;

    public AddressServiceImpl() {}

    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    @Transactional
    public List<Address> getAddresses() {
        return addressDao.getAddresses();
    }

    @Override
    @Transactional
    public Address getAddress(Integer id) {
        return addressDao.getAddress(id);
    }

    @Override
    @Transactional
    public void saveAddress(Address address) {
        addressDao.saveAddress(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Integer id) {
        addressDao.deleteAddress(id);
    }
}
