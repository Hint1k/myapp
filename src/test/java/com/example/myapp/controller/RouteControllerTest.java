package com.example.myapp.controller;

import com.example.myapp.testData.MapData;
import com.example.myapp.entity.Address;
import com.example.myapp.rest.weatherJsonParsing.Coordinates;
import com.example.myapp.service.AddressService;
import com.example.myapp.service.CourierService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// unit test of controller layer
@WebMvcTest(RouteController.class)
/* spring security requires a user with a password,
also need to set a specific "user1" with digits in the username
because of Principal attribute and how the controller works */
@WithMockUser(username = "user1")
public class RouteControllerTest {

    @MockBean
    private AddressService addressService;

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private Map<Address, Coordinates> map;

    private static Map<Address, Coordinates> mapStatic;

    private List<Address> addresses;

    private static List<Address> addressesStatic;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension
            .newInstance()
            .options(wireMockConfig().dynamicPort()
                    .usingFilesUnderClasspath("wiremock"))
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("google.api.url", wireMockExtension::baseUrl);
    }

    @BeforeAll
    public static void createData() {
        MapData mapData = new MapData();
        mapStatic = mapData.getMap();
        addressesStatic = mapData.getAddresses();
    }

    @BeforeEach
    public void setupData() {
        // in case of any changes
        addresses = addressesStatic;
        map = mapStatic;
    }

    @Test
    public void testGetAddresses() {
        when(addressService.getAddresses()).thenReturn(addresses);

        // testing
        try {
            mockMvc.perform(get("/api/courier/addresses"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("addresses", hasSize(6)))
                    .andExpect(model().attribute("addresses", hasItem(allOf(
                            hasProperty("streetName", equalTo("Main street")),
                            hasProperty("houseNumber", equalTo("11")),
                            hasProperty("countryName", equalTo("Russia"))
                    ))))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetAddresses() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testGetCoordinates() {
        when(addressService.getAddresses()).thenReturn(addresses);
        testGetAddresses();

        String fileName = "countries/{{regexExtract request.query.address '[a-zA-Z]{3}'}}";

        // stubbing with WireMock
        wireMockExtension.stubFor(WireMock.get(urlMatching("/maps/api/geocode/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(fileName + ".json")
                        .withTransformers("response-template")));

        // testing
        try {
            mockMvc.perform(get("/api/courier/coordinates"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("addresses", hasSize(6)))
                    .andExpect(model().attribute("map", aMapWithSize(6)))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testGetCoordinates() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testCalculateRoute() {
        when(addressService.getAddresses()).thenReturn(addresses);
        testGetCoordinates();

        // testing
        try {
            mockMvc.perform(get("/api/courier/distance"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("route"))
                    .andExpect(model().attribute("route", hasSize(7)))
                    .andExpect(model().attribute("distance", equalTo("7832 km")))
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testCalculateRoute() fails");
            throw new RuntimeException(e);
        }

        verify(addressService, times(1)).getAddresses();
    }

    @Test
    public void testSaveRouteToUserDevice() {
        // Mocking test data
        List<String> mockRoute = Arrays
                .asList("Address 1", "Address 2", "Address 3");
        double mockDistance = 100;

        // Creating the expected file content
        String fileContent = """
                Your route is:
                Address 1
                Address 2
                Address 3
                Total distance is:
                100""";

        // Mocking HttpServletResponse
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Creating an instance of the actual controller
        RouteController controller = new RouteController(
                "googleApiUrl", "googleApiKey");

        // Using Reflection to set the private fields "route" and "distance"
        ReflectionTestUtils.setField(controller, "route", mockRoute);
        ReflectionTestUtils.setField(controller, "distance", (int) mockDistance);

        // Invoking the method under test
        String viewName = controller
                .saveRouteToUserDevice(response, new ExtendedModelMap());

        // Asserting the method returns null, indicating successful file download
        assertNull(viewName);

        // Asserting the file content, the content type and headers
        assertEquals("text/plain", response.getContentType());
        assertEquals("attachment; filename=\"route.txt\"",
                response.getHeader("Content-Disposition"));
        try {
            assertEquals(fileContent, response.getContentAsString().trim());
        } catch (UnsupportedEncodingException e) {
            System.out.println("testSaveRouteToUserDevice() fails");
            throw new RuntimeException(e);
        }
    }
}