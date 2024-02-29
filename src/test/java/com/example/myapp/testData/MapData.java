package com.example.myapp.testData;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapData {

    // creating a map to store a list of cities with their coordinates
    private Map<Address, Coordinates> map;

    private List<Address> addresses;

    private void createListOfLocations() {
        map = new LinkedHashMap<>();

        AddressData addressData = new AddressData();
        addresses = addressData.getAddressData();

        List<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(55.755826, 37.617300));
        coordinates.add(new Coordinates(48.856614, 2.3522219));
        coordinates.add(new Coordinates(51.540355, -0.143268));
        coordinates.add(new Coordinates(52.520006, 13.404954));
        coordinates.add(new Coordinates(41.902783, 12.496365));
        coordinates.add(new Coordinates(40.416775, -3.703790));

        int courierId = 1;
        Courier courier = new Courier();
        courier.setId(courierId);

        for (int i = 0; i < addresses.size(); i++) {
            addresses.get(i).setCourier(courier);
        }

        for (int i = 0; i < addresses.size(); i++) {
            map.put(addresses.get(i), coordinates.get(i));
        }

    }

    public MapData() {
        createListOfLocations();
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<Address, Coordinates> getMap() {
        return map;
    }

    public void setMap(Map<Address, Coordinates> map) {
        this.map = map;
    }
}