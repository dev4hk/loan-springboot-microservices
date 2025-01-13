package com.example.gateway_server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    @RequestMapping("/error")
    public Mono<String> errorMessage() {
        return Mono.just("An error occurred. Please try after some time!!!");
    }
}
