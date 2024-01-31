package com.example.myapp.data;

import com.example.myapp.entity.Courier;

import java.util.ArrayList;
import java.util.List;

public class CourierData {

    private List<Courier> couriers;

    private void createCourierList() {
        couriers = new ArrayList<>();
        Courier courier1 = new Courier(
                1, "John", "Doe", "+79991111111");
        Courier courier2 = new Courier(
                2, "Jane", "Doe", "+79991111122");
        Courier courier3 = new Courier(
                3, "Jack", "Black", "+79991111133");
        Courier courier4 = new Courier(
                4, "Mary", "Sue", "+79991111144");
        Courier courier5 = new Courier(
                5, "Martin", "Sue", "+79991111155");

        couriers.add(courier1);
        couriers.add(courier2);
        couriers.add(courier3);
        couriers.add(courier4);
        couriers.add(courier5);
    }

    public CourierData() {
        createCourierList();
    }

    public List<Courier> getCourierData() {
        return couriers;
    }

    public void setCourierData(List<Courier> couriers) {
        this.couriers = couriers;
    }
}