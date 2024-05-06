package com.example.myapp.restController;

import com.example.myapp.entity.Address;
import com.example.myapp.rest.restController.ValidationRestController;
import com.example.myapp.service.CourierService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// unit test of rest controller
@WebMvcTest(ValidationRestController.class)
@WithMockUser // spring security requires a user with a password
public class ValidationRestControllerTest {

    @MockBean
    private CourierService courierService;

    @Autowired
    private MockMvc mockMvc;

    private static final String GOOGLE_ADDRESS
            = "/v1:validateAddress?key=";

    private static final String GOOGLE_API_KEY
            = "googleApiKey";


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("validation.api.url", wireMockExtension::baseUrl);
        registry.add("google.api.key", () -> GOOGLE_API_KEY);
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension
            .newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .notifier(new ConsoleNotifier(true))
                    .usingFilesUnderClasspath("wiremock"))
            .build();

    @Test
    public void testValidateAddress() throws IOException {
        String urlString = GOOGLE_ADDRESS + GOOGLE_API_KEY;

        // stubbing Google response with WireMock
        wireMockExtension.stubFor(WireMock.post(urlEqualTo(urlString))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("validation.json")));

        // mocking user input
        Address address = new Address();
        address.setCountryName("UK");
        address.setCityName("London");
        address.setStreetName("Downing Street");
        address.setHouseNumber("10");

        // testing
        try {
            mockMvc.perform(post("/api/customers/order")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .flashAttr("address", address)
                    )
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
        } catch (Exception e) {
            System.out.println("testValidateAddress() fails");
            throw new RuntimeException(e);
        }

        wireMockExtension.verify(1, postRequestedFor(urlEqualTo(urlString)));
    }
}