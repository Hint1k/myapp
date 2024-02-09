package com.example.myapp.route;

import com.example.myapp.testData.MapData;
import com.example.myapp.entity.Address;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteCalculationTest {

    RouteCalculation routeCalculation;

    private Map<Address, Coordinates> map;

    private void createData() {
        MapData mapData = new MapData();
        map = mapData.getMap();
    }

    @Test
    public void routeCalculation() {

        // testing
        createData();
        routeCalculation = new RouteCalculation(map);
        double distance = routeCalculation.getDistance();
        distance = (int) distance;

        assertEquals(distance, 7839);
    }
}