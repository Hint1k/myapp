package com.example.myapp.route;

import java.util.*;

// currently there is no front-end for this route calculation
public class RouteCalculation {

    // creating a map to store a list of cities with their coordinates
    public static Map<City, CityCoordinates> map;

    // creating indexed list of the "map", copy the references into an ArrayList
    public static List<Map.Entry<City, CityCoordinates>> indexedList;

    // creating array to save distances between cities
    public static List<Double> distances;

    // Distance matrix (distances between cities in the matrix);
    public static double[][] matrix;

    public static void main(String[] args) {

        createListOfDistancesAndListOfCities();
        createDistanceMatrix();

        System.out.println("------------");
        String[] distancePrecise =
                String.valueOf(calculateShortestPath()).split("\\.");
        String distanceApproximated = distancePrecise[0];
        System.out.println(distanceApproximated + " kilometers");

        //printing out indexedList to check it is correctly filled in
        System.out.println("-------------");
        indexedList = new ArrayList<>(map.entrySet());
        for (int i = 0; i < map.size(); i++) {
            Map.Entry<City, CityCoordinates> entry = indexedList.get(i);
            String city = entry.getKey().getCityName();
            double latitude = entry.getValue().getLatitude();
            double longitude = entry.getValue().getLongitude();
            System.out.println(city + " : " + latitude + ", " + longitude);
        }
    }

    public static double calculateDistanceBetweenCities
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

    public static void createListOfDistancesAndListOfCities() {
        CityData cityData = new CityData();
        map = cityData.getMap();
        //creating ArrayList to iterate "map" not knowing keys and/or values
        indexedList = new ArrayList<>(map.entrySet());
        distances = new ArrayList<>();

        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.size(); j++) {
                if (i < j) {

                    Map.Entry<City, CityCoordinates> entryI = indexedList.get(i);
                    Map.Entry<City, CityCoordinates> entryJ = indexedList.get(j);

                    CityCoordinates cityCoordinatesI = entryI.getValue();
                    CityCoordinates cityCoordinatesJ = entryJ.getValue();

                    double latitudeI = cityCoordinatesI.getLatitude();
                    double latitudeJ = cityCoordinatesJ.getLatitude();
                    double longitudeI = cityCoordinatesI.getLongitude();
                    double longitudeJ = cityCoordinatesJ.getLongitude();

                    double distance = calculateDistanceBetweenCities
                            (latitudeI, longitudeI, latitudeJ, longitudeJ);

                    distances.add(distance);
                }
            }
        }
    }

    // creating a matrix of distances between cities
    public static double[][] createDistanceMatrix() {
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

        // printing out matrix to check the result visually
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                // removing everything after "." to make it readable
                String string = String.valueOf(matrix[i][j]);
                String[] stringArray = string.split("\\.");
                String distance = stringArray[0];
                System.out.print(distance + "   ");
            }
            System.out.println();
        }
        return matrix;
    }

    private static int getVariable(int counter) {
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

    private static double calculateShortestPath() {
        double minimum = Double.MAX_VALUE;
        double distance = 0.0;
        List<String> cities = new ArrayList<>();
        List<String> path = new ArrayList<>();
        for (City city : map.keySet()) {
            cities.add(city.getCityName());
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
        path.forEach(System.out::println);
        return distance;
    }
}