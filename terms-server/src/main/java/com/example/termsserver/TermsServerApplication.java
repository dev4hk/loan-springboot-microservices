package com.example.termsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class TermsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TermsServerApplication.class, args);
    }

}
