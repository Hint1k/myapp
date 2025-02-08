package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
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
                .uri(URI.create("http://localhost:8080/actuator/health"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        int retries = 10;
        while (retries > 0) {
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    Thread.sleep(5000); // Wait for logs to settle down
                    return;
                }
            } catch (IOException e) {
                System.out.println("Waiting for http://localhost:8080/actuator/health...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                System.out.println("Thread was interrupted. Exiting...");
                throw new RuntimeException("Thread interrupted while waiting for service.");
            }

            retries--;
            try {
                Thread.sleep(3000); // Pause before retrying
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Application interrupted during wait. Exiting...");
                throw new RuntimeException("Application interrupted during wait.");
            }
        }

        System.out.println("Service did not respond after multiple retries. Exiting...");
        throw new RuntimeException("Service did not respond after multiple retries.");
    }
}