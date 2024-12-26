package com.example.applicationserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Application microservice",
                description = "Application microservice REST API Documentation",
                version = "v1"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Application microservice REST API Documentation"
        )
)
@SpringBootApplication
public class ApplicationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationServerApplication.class, args);
    }

}
