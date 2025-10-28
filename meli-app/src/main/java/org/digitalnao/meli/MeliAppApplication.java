package org.digitalnao.meli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class that serves as the entry point for the Spring Boot application.
 * This class uses the @SpringBootApplication annotation to enable auto-configuration,
 * component scanning, and Spring Boot features. It bootstraps the entire application
 * context, initializes all configured beans, and starts the embedded web server,
 * making the application ready to handle incoming HTTP requests.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@SpringBootApplication
public class MeliAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeliAppApplication.class, args);
    }
}
