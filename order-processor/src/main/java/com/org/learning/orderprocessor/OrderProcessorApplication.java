package com.org.learning.orderprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class OrderProcessorApplication {

    public static void main(String[] args) {
        var application = new SpringApplication(OrderProcessorApplication.class);
        // config is per-profile only (application-<profile>.yml) — fall back to local
        application.setDefaultProperties(Map.of("spring.profiles.default", "local"));
        application.run(args);
    }
}
