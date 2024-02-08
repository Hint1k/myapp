package com.example.myapp.route;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;

import java.util.*;

// currently there is no front-end for this route calculation
public class RouteCalculation {

    // creating a map to store a list of cities with their coordinates
    private Map<Address, Coordinates> map;

    // creating array to save distances between cities
    private List<Double> distances;

    private double[][] matrix;

    private double distance;

    private List<String> path;

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public RouteCalculation(Map<Address, Coordinates> cities) {
        createListOfDistancesAndListOfCities(cities);
        createDistanceMatrix();
        calculateShortestPath();
    }

    private double calculateDistanceBetweenCities
            (double lat1, double lon1, double lat2, double lon2) {

        int earth_radius = 6371; // km
        double converter = Math.PI / 180; // degrees into radians

        // Haversine equation to calculate distance between two points
        double distance = 2 * earth_radius * Math.asin(Math.sqrt(0.5 -
                Math.cos((lat2 - lat1) * converter) / 2 +
                Math.cos(lat1 * converter) * Math.cos(lat2 * converter) *
                        (1 - Math.cos((lon2 - lon1) * converter)) / 2));

        return distance;
    }

    private void createListOfDistancesAndListOfCities(Map<Address, Coordinates> cities) {
        distances = new ArrayList<>();
        map = cities;
        //creating ArrayList to iterate "map" by index, since keys and/or values are unknown
        List<Map.Entry<Address, Coordinates>> indexedList = new ArrayList<>(map.entrySet());

        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.size(); j++) {
                if (i < j) {

                    Map.Entry<Address, Coordinates> entryI = indexedList.get(i);
                    Map.Entry<Address, Coordinates> entryJ = indexedList.get(j);

                    Coordinates coordinatesI = entryI.getValue();
                    Coordinates coordinatesJ = entryJ.getValue();

                    double latitudeI = coordinatesI.getLatitude();
                    double latitudeJ = coordinatesJ.getLatitude();
                    double longitudeI = coordinatesI.getLongitude();
                    double longitudeJ = coordinatesJ.getLongitude();

                    double distance = calculateDistanceBetweenCities
                            (latitudeI, longitudeI, latitudeJ, longitudeJ);

                    distances.add(distance);
                }
            }
        }
    }

    // creating a matrix of distances between cities
    private void createDistanceMatrix() {
        matrix = new double[map.size()][map.size()];
        int k = 0;
        int m = 0;
        for (int i = 0; i < matrix.length; i++) {
            k = getVariable(i);
            for (int j = 0; j < matrix.length; j++) {
                m = getVariable(j);
                if (i == j) {
                    matrix[i][j] = 0;
                } else if (i < j) {
                    matrix[i][j] = distances.get(j - 1 + k);
                } else {
                    matrix[i][j] = distances.get(i - 1 + m);
                }
            }
        }
    }

    private int getVariable(int counter) {
        int variable = 0;
        double step = 1.5;
        int numberOfCities = map.size();
        for (int i = 0; i < counter + 1; i++) {
            // my own equation to fill in the distance matrix correctly
            variable = (int) (counter * (numberOfCities - step));
            step += 0.5;
        }
        return variable;
    }

    private void calculateShortestPath() {
        double minimum = Double.MAX_VALUE;
        List<String> cities = new ArrayList<>();
        path = new ArrayList<>();
        for (Address address : map.keySet()) {
            cities.add(address.getCityName());
        }
        path.add(cities.get(0)); // adding starting city
        String nearestCity = null;
        int lastCityIndex = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != j && matrix[i][j] < minimum
                        && !path.contains(cities.get(j))) {
                    minimum = matrix[i][j];
                    nearestCity = cities.get(j);
                    lastCityIndex = j;
                }
            }
            distance += minimum;
            path.add(nearestCity);
            minimum = Double.MAX_VALUE;
            i = lastCityIndex - 1;
            // breaking cycle condition
            if (path.size() == map.size()) {
                // adding distance to return back to the finishing city
                distance += matrix[0][lastCityIndex];
                // adding finishing city to complete path
                path.add(cities.get(0));
                break;
            }
        }
    }
}