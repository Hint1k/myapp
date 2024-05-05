package com.example.myapp.controller;

import com.example.myapp.entity.Address;
import com.example.myapp.entity.Courier;
import com.example.myapp.rest.locationJsonParsing.LocationResponse;
import com.example.myapp.rest.restController.LocationRestController;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;
import com.example.myapp.route.RouteCalculation;
import com.example.myapp.service.AddressService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/api/courier")
public class RouteController {

    private Map<Address, Coordinates> map;

    private List<Address> addresses;

    @Autowired
    private AddressService addressService;

    private final String googleApiUrl;

    private final String googleApiKey;

    private int distance;

    private List<String> route;

    public RouteController(@Value("${google.api.url}")
                           String googleApiUrl,
                           @Value("${google.api.key}")
                           String googleApiKey) {
        this.googleApiUrl = googleApiUrl;
        this.googleApiKey = googleApiKey;
    }

    @GetMapping("/addresses")
    public String getAddresses(Principal principal, Model model) {
        String username = principal.getName();
        username = username.replaceAll("\\D+", "");
        int courierId = Integer.parseInt(username);
        addresses = addressService.getAddresses();
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            Courier courier = address.getCourier();
            int id = courier.getId();
            if (courierId != id) {
                addresses.remove(address);
                i--;
            }
        }
        if (addresses.isEmpty()) {
            String message = "No addresses found for you." +
                    " Contact your manager";
            model.addAttribute("message", message);
        } else {
            model.addAttribute("addresses", addresses);
            model.addAttribute("addressesLoaded", true);
        }
        return "route";
    }

    @GetMapping("/coordinates")
    public String getCoordinates(Model model) {
        map = new LinkedHashMap<>();

        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);

            LocationResponse response = getLocationResponse(address);

            double latitude = response.getResults()[0]
                    .getGeometry().getLocation().getLatitude();
            double longitude = response.getResults()[0]
                    .getGeometry().getLocation().getLongitude();

            Coordinates coordinates = new Coordinates(latitude, longitude);

            map.put(address, coordinates);
        }

        model.addAttribute("map", map);
        model.addAttribute("addresses", addresses);
        model.addAttribute("addressesLoaded", true);
        model.addAttribute("coordinatesLoaded", true);

        return "route";
    }

    private LocationResponse getLocationResponse(Address address) {
        String countryName = address.getCountryName();
        String cityName = address.getCityName();
        String streetName = address.getStreetName();
        Integer houseNumber = address.getHouseNumber();
        String stringAddress = countryName + ", " + cityName + ", "
                + streetName + ", " + houseNumber;

        LocationRestController controller =
                new LocationRestController(googleApiUrl, googleApiKey);
        LocationResponse response = controller.getLocation(stringAddress);

        return response;
    }

    @GetMapping("/distance")
    public String calculateRoute(Model model) {
        RouteCalculation routeCalculation = new RouteCalculation(map);

        double distanceDouble = routeCalculation.getDistance();
        String[] string = String.valueOf(distanceDouble).split("\\.");
        distance = Integer.parseInt(string[0]);
        route = routeCalculation.getPath();

        model.addAttribute("route", route);
        model.addAttribute("distance", distance + " km");
        model.addAttribute("map", map);
        model.addAttribute("addresses", addresses);
        model.addAttribute("addressesLoaded", true);
        model.addAttribute("coordinatesLoaded", true);
        model.addAttribute("routeLoaded", true);

        return "route";
    }

    @GetMapping("/file")
    public void saveRouteToUserDevice(HttpServletResponse response) {
        List<String> array = new ArrayList<>();
        array.add("Your route is:");
        array.addAll(route);
        array.add("Total distance is:");
        array.add(String.valueOf(distance));

        try (
                InputStream inputStream = new ByteArrayInputStream(
                        String.join("\n", array).getBytes())
        ) {
            IOUtils.copy(inputStream, response.getOutputStream());
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"route.txt\"");
            response.flushBuffer();
        } catch (IOException ioException) {
            System.out.println("Could not save the file");
            ioException.printStackTrace(System.out); // temp solution
        }
    }
}