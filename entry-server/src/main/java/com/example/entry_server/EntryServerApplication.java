package com.example.entry_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class EntryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntryServerApplication.class, args);
	}

}
