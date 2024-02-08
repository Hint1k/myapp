package com.example.myapp.data;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;

import java.util.ArrayList;
import java.util.List;

public class AddressData {

    private List<Address> addresses;
    private List<Courier> couriers;

    private void createAddressList() {
        addresses = new ArrayList<>();
        Address address1 = new Address(
                1, "Russia", "Moscow", "Main street", 10, couriers.get(0));
        Address address2 = new Address(
                2, "France", "Paris", "Side street", 11, couriers.get(1));
        Address address3 = new Address(
                3, "England", "London", "Back street", 12, couriers.get(2));
        Address address4 = new Address(
                4, "Germany", "Berlin", "Down street", 13, couriers.get(3));
        Address address5 = new Address(
                5, "Italy", "Rome", "Up street", 14, couriers.get(4));
        Address address6 = new Address(
                6, "Spain", "Madrid", "Forward street", 15, couriers.get(0));

        addresses.add(address1);
        addresses.add(address2);
        addresses.add(address3);
        addresses.add(address4);
        addresses.add(address5);
        addresses.add(address6);
    }

    public AddressData() {
        CourierData courierData = new CourierData();
        couriers = courierData.getCourierData();
        createAddressList();
    }

    public List<Address> getAddressData() {
        return addresses;
    }

    public void setAddressData(List<Address> addresses) {
        this.addresses = addresses;
    }
}