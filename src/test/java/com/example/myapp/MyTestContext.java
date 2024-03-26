package com.example.myapp;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

/* I needed this class to avoid an error of the application context
loaded twice for two test classes WeatherControllerTest and
LocationRestControllerTest when I run maven lifecycle test */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyTestContext {

    private static WebApplicationContext context;

    @BeforeAll
    public static void setUp() {
        if (context == null) {
            context = (WebApplicationContext) SpringApplication
                    .run(MyappApplication.class);
        }
    }

    public static WebApplicationContext getContext() {
        return context;
    }
}