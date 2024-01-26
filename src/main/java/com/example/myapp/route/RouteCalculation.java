package com.example.myapp.route;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;


import java.util.*;

// currently there is no front-end for this route calculation
public class RouteCalculation {
    // creating a map to store a list of cities with their coordinates
    public static Map<City, CityCoordinates> map;

    // creating indexed list of the "map", copy the references into an ArrayList
    public static List<Map.Entry<City, CityCoordinates>> indexedList;

    // creating array to save distances between cities
    public static List<Double> distances;

    // creating array to save city pairs
    public static MultivaluedMap<String, String> cities;

    // creating united map for city pairs and distances between them
    public static Map<String, Double> unitedMap;

    // counting number of cities
    public static int numberOfCities;

    // Distance matrix (distances between cities in the matrix);
    public static double[][] matrix;

    public static void main(String[] args) {

        createListOfDistancesAndListOfCities();
        createDistanceMatrix();

        System.out.println("------------");
        String[] distancePrecise = String.valueOf(calculateShortestPath()).split("\\.");
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

    public static Map<City, CityCoordinates> createListOfLocations() {

        // filling in the "map" with cities and coordinates imitating front-end input
        map = new LinkedHashMap<>();

        City city0 = new City("Moscow"); // start and finish is here
        City city1 = new City("Vladimir");
        City city2 = new City("Tver");
        City city3 = new City("Ryazan");
        City city4 = new City("Tula");
        City city5 = new City("Kaluga");
//        City city6 = new City("TestCity");

        CityCoordinates coordinates0 = new CityCoordinates(55.75, 37.61);
        CityCoordinates coordinates1 = new CityCoordinates(56.14, 40.38);
        CityCoordinates coordinates2 = new CityCoordinates(56.85, 35.91);
        CityCoordinates coordinates3 = new CityCoordinates(54.61, 39.71);
        CityCoordinates coordinates4 = new CityCoordinates(54.20, 37.61);
        CityCoordinates coordinates5 = new CityCoordinates(54.51, 36.26);
//        CityCoordinates coordinates6 = new CityCoordinates(53.50, 40.38);

        map.put(city0, coordinates0);
        map.put(city1, coordinates1);
        map.put(city2, coordinates2);
        map.put(city3, coordinates3);
        map.put(city4, coordinates4);
        map.put(city5, coordinates5);
//        map.put(city6, coordinates6);

        numberOfCities = map.size();
        return map;
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
        map = createListOfLocations();
        indexedList = new ArrayList<>(map.entrySet());
        distances = new ArrayList<>();
        cities = new MultivaluedHashMap<>();
        unitedMap = new LinkedHashMap<>();

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
                    //System.out.println(distance);
                    distances.add(distance);

                    String cityI = entryI.getKey().getCityName();
                    String cityJ = entryJ.getKey().getCityName();
                    //System.out.println(cityI + " - " + cityJ);
                    cities.add(cityI, cityJ);

                    unitedMap.put(cityI + "->" + cityJ, distance);
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
            if (path.size() == numberOfCities) {
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