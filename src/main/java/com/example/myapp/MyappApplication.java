package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@SpringBootApplication
public class MyappApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyappApplication.class, args);
        waitForService();
        System.out.println("\n✅ Application started! Go to: \033[34mhttp://localhost:8080\033[0m\n");
    }

    private static void waitForService() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/health"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        int retries = 12;
        while (retries > 0) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) return;
            } catch (Exception e) {
                System.out.println("Waiting for http://localhost:8080/health...");
            }
            retries--;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}