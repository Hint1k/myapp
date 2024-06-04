package com.example.myapp.route;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;

import java.util.*;

public class RouteCalculation {

    // a map to store a list of addresses with their coordinates
    private Map<Address, Coordinates> map;

    // an array to save distances between addresses
    private List<Double> distances;

    // a matrix to store distances between addresses
    private double[][] matrix;

    // total distance in kilometers
    private double distance;

    // total path = list of addresses to visit
    private List<String> path;

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public RouteCalculation(Map<Address, Coordinates> addresses) {
        createListOfDistancesAndListOfAddresses(addresses);
        createDistanceMatrix();
        calculateShortestPath();
    }

    private double calculateDistanceBetweenAddresses
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

    private void createListOfDistancesAndListOfAddresses(Map<Address, Coordinates> addresses) {
        distances = new ArrayList<>();
        map = addresses;
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

                    double distance = calculateDistanceBetweenAddresses
                            (latitudeI, longitudeI, latitudeJ, longitudeJ);

                    distances.add(distance);
                }
            }
        }
    }

    // creating a matrix of distances between addresses
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

    /* In order to put the values from the list of distances to the distance matrix
     in their correct places it is necessary to calculate this variable.
     It helps to move "i" and "j" counters in the createDistanceMatrix() method
     to their correct positions */
    private int getVariable(int counter) {
        int variable = 0;
        double step = 1.5;
        int numberOfAddresses = map.size();
        for (int i = 0; i < counter + 1; i++) {
            // my own equation to fill in the distance matrix correctly
            variable = (int) (counter * (numberOfAddresses - step));
            step += 0.5;
        }
        return variable;
    }

    // greedy algorithm
    private void calculateShortestPath() {
        // the case with map.size == 0 is handled by RouteController

        // the case when there is only one address present
        if (map.size() == 1) {
            distance = 0.0;
            path = new ArrayList<>();
            String address = map.keySet().iterator().next().getCountryName() + ", " +
                    map.keySet().iterator().next().getCityName() + ", " +
                    map.keySet().iterator().next().getStreetName() + ", " +
                    map.keySet().iterator().next().getHouseNumber();
            path.add(address);
            return;
        }

        // handle all other cases
        double minimum = Double.MAX_VALUE;
        List<String> addresses = new ArrayList<>();
        path = new ArrayList<>();
        for (Address address : map.keySet()) {
            addresses.add(address.getCountryName() + ", " + address.getCityName() + ", "
                    + address.getStreetName() + ", " + address.getHouseNumber());
        }
        path.add(addresses.get(0)); // adding starting address
        String nearestAddress = null;
        int lastAddressIndex = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != j && matrix[i][j] < minimum
                        && !path.contains(addresses.get(j))) {
                    minimum = matrix[i][j];
                    nearestAddress = addresses.get(j);
                    lastAddressIndex = j;
                }
            }
            distance += minimum;
            path.add(nearestAddress);
            minimum = Double.MAX_VALUE;
            i = lastAddressIndex - 1;
            // breaking cycle condition
            if (path.size() == map.size()) {
                // adding distance to return back to the last address
                distance += matrix[0][lastAddressIndex];
                // adding the last address to complete the path
                path.add(addresses.get(0));
                break;
            }
        }
    }
}