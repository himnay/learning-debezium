package com.org.learning.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        var application = new SpringApplication(OrderServiceApplication.class);
        // config is per-profile only (application-<profile>.yml) — fall back to local
        application.setDefaultProperties(Map.of("spring.profiles.default", "local"));
        application.run(args);
    }
}
