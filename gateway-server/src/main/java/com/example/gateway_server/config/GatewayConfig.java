package com.example.gateway_server.config;

import com.example.gateway_server.filter.AddUsernameHeaderGatewayFilterFactory;
import com.example.gateway_server.filter.AddUsernameHeaderGatewayFilterFactory.Config;
import com.example.gateway_server.filter.BlockAccessFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator loanRouteConfig(RouteLocatorBuilder routeLocatorBuilder, BlockAccessFilter blockAcceptTermsAccessFilter, AddUsernameHeaderGatewayFilterFactory addUsernameHeaderGatewayFilterFactory) {
        return routeLocatorBuilder.routes()
//                .route(p -> p
//                        .path("/loan/accept-terms/**")
//                        .filters(f -> f.rewritePath("/loan/accept-terms/(?<segment>.*)", "/${segment}")
//                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//                                .filter(blockAcceptTermsAccessFilter.apply(new BlockAccessFilter.Config()))
//                                .circuitBreaker(config -> config.setName("acceptTermsCircuitBreaker")
//                                        .setFallbackUri("forward:/error")
//                                )
//                        )
//                        .uri("lb://ACCEPT-TERMS-SERVER"))
                .route("block-accept-terms-route", r -> r
                        .path("/ACCEPT-TERMS-SERVER/**")
                        .filters(f -> f.filter(blockAcceptTermsAccessFilter.apply(new BlockAccessFilter.Config())))
                        .uri("no://op"))
                .route(p -> p
                        .path("/loan/application/**")
                        .filters(f -> f.rewritePath("/loan/application/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("applicationCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://APPLICATION-SERVER"))
                .route(p -> p
                        .path("/loan/balance/**")
                        .filters(f -> f.rewritePath("/loan/balance/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .filter(blockAcceptTermsAccessFilter.apply(new BlockAccessFilter.Config()))
                                .circuitBreaker(config -> config.setName("balanceCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
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
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://COUNSEL-SERVER"))
                .route(p -> p
                        .path("/loan/entry/**")
                        .filters(f -> f.rewritePath("/loan/entry/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("entryCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://ENTRY-SERVER"))
                .route(p -> p
                        .path("/loan/file-storage/**")
                        .filters(f -> f.rewritePath("/loan/file-storage/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("fileStorageCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://FILE-STORAGE-SERVER"))
                .route(p -> p
                        .path("/loan/judgement/**")
                        .filters(f -> f.rewritePath("/loan/judgement/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("judgementCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
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
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://REPAYMENT-SERVER"))
                .route(p -> p
                        .path("/loan/terms/**")
                        .filters(f -> f.rewritePath("/loan/terms/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("termsCircuitBreaker")
                                        .setFallbackUri("forward:/error")
                                )
                                .filter(addUsernameHeaderGatewayFilterFactory.apply(new Config()))
                        )
                        .uri("lb://TERMS-SERVER"))
                .build();
    }
}
