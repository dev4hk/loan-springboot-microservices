package com.example.entry_server;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
		info = @Info(
				title = "Entry microservice",
				description = "Entry microservice REST API Documentation",
				version = "v1"
		),
		externalDocs = @ExternalDocumentation(
				description = "Entry microservice REST API Documentation"
		)
)
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class EntryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntryServerApplication.class, args);
	}

}
