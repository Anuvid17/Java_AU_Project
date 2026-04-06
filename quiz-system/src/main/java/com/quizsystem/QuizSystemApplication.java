package com.quizsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Online Quiz System application.
 * Bootstraps Spring Boot with auto-configuration for Web, JPA, Security, and Thymeleaf.
 */
@SpringBootApplication
public class QuizSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizSystemApplication.class, args);
    }
}
