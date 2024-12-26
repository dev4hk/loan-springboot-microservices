package com.example.counselserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
		info = @Info(
				title = "Counsel microservice",
				description = "Counsel microservice REST API Documentation",
				version = "v1"
		),
		externalDocs = @ExternalDocumentation(
				description = "Counsel microservice REST API Documentation"
		)
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class CounselServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounselServerApplication.class, args);
	}

}
