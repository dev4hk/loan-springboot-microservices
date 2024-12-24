package com.example.counselserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class CounselServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounselServerApplication.class, args);
	}

}
