package com.example.accepttermsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@SpringBootApplication
public class AcceptTermsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcceptTermsServerApplication.class, args);
    }

}
