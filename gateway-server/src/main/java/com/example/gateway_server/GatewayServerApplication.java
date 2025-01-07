package com.example.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator loanRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/loan/accept-terms/**")
                        .filters(f -> f.rewritePath("/loan/accept-terms/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("acceptTermsCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://ACCEPT-TERMS-SERVER"))
                .route(p -> p
                        .path("/loan/application/**")
                        .filters(f -> f.rewritePath("/loan/application/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("applicationCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://APPLICATION-SERVER"))
                .route(p -> p
                        .path("/loan/balance/**")
                        .filters(f -> f.rewritePath("/loan/balance/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("balanceCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://BALANCE-SERVER"))
                .route(p -> p
                        .path("/loan/counsel/**")
                        .filters(f -> f.rewritePath("/loan/counsel/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("counselCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)
                                )
                        )
                        .uri("lb://COUNSEL-SERVER"))
                .route(p -> p
                        .path("/loan/entry/**")
                        .filters(f -> f.rewritePath("/loan/entry/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("entryCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://ENTRY-SERVER"))
                .route(p -> p
                        .path("/loan/file-storage/**")
                        .filters(f -> f.rewritePath("/loan/file-storage/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("fileStorageCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://FILE-STORAGE-SERVER"))
                .route(p -> p
                        .path("/loan/judgement/**")
                        .filters(f -> f.rewritePath("/loan/judgement/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://JUDGEMENT-SERVER"))
                .route(p -> p
                        .path("/loan/repayment/**")
                        .filters(f -> f.rewritePath("/loan/repayment/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("repaymentCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        .retry(retryConfig -> retryConfig.setRetries(3).setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)
                                )
                        )
                        .uri("lb://REPAYMENT-SERVER"))
                .route(p -> p
                        .path("/loan/terms/**")
                        .filters(f -> f.rewritePath("/loan/terms/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("termsCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                        )
                        .uri("lb://TERMS-SERVER"))
                .build();
    }

}
