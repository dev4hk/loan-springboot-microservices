package com.example.accepttermsserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
        info = @Info(
                title = "Accept Terms microservice",
                description = "Accept Terms microservice REST API Documentation",
                version = "v1"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Accept Terms microservice REST API Documentation"
        )
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class AcceptTermsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcceptTermsServerApplication.class, args);
    }

}
