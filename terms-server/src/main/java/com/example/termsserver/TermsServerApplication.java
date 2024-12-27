package com.example.termsserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
        info = @Info(
                title = "Terms microservice",
                description = "Terms microservice REST API Documentation",
                version = "v1"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Terms microservice REST API Documentation"
        )
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class TermsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TermsServerApplication.class, args);
    }

}
