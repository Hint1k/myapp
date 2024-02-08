package com.example.myapp.data;

import com.example.myapp.rest.weatherJsonParsing.Coordinates;

import java.util.LinkedHashMap;
import java.util.Map;

public class CityData {

    // creating a map to store a list of cities with their coordinates
    public static Map<City, Coordinates> map;

    public static void createListOfLocations() {

        // filling in the "map" with cities and coordinates imitating front-end input
        map = new LinkedHashMap<>();

        City city0 = new City("Moscow"); // start and finish is here
        City city1 = new City("Vladimir");
        City city2 = new City("Tver");
        City city3 = new City("Ryazan");
        City city4 = new City("Tula");
        City city5 = new City("Kaluga");
//        City city6 = new City("TestCity");

        Coordinates coordinates0 = new Coordinates(55.75, 37.61);
        Coordinates coordinates1 = new Coordinates(56.14, 40.38);
        Coordinates coordinates2 = new Coordinates(56.85, 35.91);
        Coordinates coordinates3 = new Coordinates(54.61, 39.71);
        Coordinates coordinates4 = new Coordinates(54.20, 37.61);
        Coordinates coordinates5 = new Coordinates(54.51, 36.26);
//        CityCoordinates coordinates6 = new CityCoordinates(53.50, 40.38);

        map.put(city0, coordinates0);
        map.put(city1, coordinates1);
        map.put(city2, coordinates2);
        map.put(city3, coordinates3);
        map.put(city4, coordinates4);
        map.put(city5, coordinates5);
//        map.put(city6, coordinates6);
    }

    public CityData() {
        createListOfLocations();
    }

    public static Map<City, Coordinates> getMap() {
        return map;
    }

    public static void setMap(Map<City, Coordinates> map) {
        CityData.map = map;
    }
}