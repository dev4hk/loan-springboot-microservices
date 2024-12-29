package com.example.filestorageserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "File storage microservice",
                description = "File storage microservice REST API Documentation",
                version = "v1"
        ),
        externalDocs = @ExternalDocumentation(
                description = "File storage microservice REST API Documentation"
        )
)
@SpringBootApplication
public class FileStorageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageServerApplication.class, args);
    }

}
