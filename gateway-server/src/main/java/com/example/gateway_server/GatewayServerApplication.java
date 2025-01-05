package com.example.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

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
						.filters( f -> f.rewritePath("/loan/accept-terms/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("acceptTermsCircuitBreaker"))
						)
						.uri("lb://ACCEPT-TERMS-SERVER"))
				.route(p -> p
						.path("/loan/application/**")
						.filters( f -> f.rewritePath("/loan/application/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("applicationCircuitBreaker")))
						.uri("lb://APPLICATION-SERVER"))
				.route(p -> p
						.path("/loan/balance/**")
						.filters( f -> f.rewritePath("/loan/balance/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("balanceCircuitBreaker")))
						.uri("lb://BALANCE-SERVER"))
				.route(p -> p
						.path("/loan/counsel/**")
						.filters( f -> f.rewritePath("/loan/counsel/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("counselCircuitBreaker")))
						.uri("lb://COUNSEL-SERVER"))
				.route(p -> p
						.path("/loan/entry/**")
						.filters( f -> f.rewritePath("/loan/entry/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("entryCircuitBreaker")))
						.uri("lb://ENTRY-SERVER"))
				.route(p -> p
						.path("/loan/file-storage/**")
						.filters( f -> f.rewritePath("/loan/file-storage/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("fileStorageCircuitBreaker")))
						.uri("lb://FILE-STORAGE-SERVER"))
				.route(p -> p
						.path("/loan/judgement/**")
						.filters( f -> f.rewritePath("/loan/judgement/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("judgementCircuitBreaker")))
						.uri("lb://JUDGEMENT-SERVER"))
				.route(p -> p
						.path("/loan/repayment/**")
						.filters( f -> f.rewritePath("/loan/repayment/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("repaymentCircuitBreaker")))
						.uri("lb://REPAYMENT-SERVER"))
				.route(p -> p
						.path("/loan/terms/**")
						.filters( f -> f.rewritePath("/loan/terms/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
						.circuitBreaker(config -> config.setName("termsCircuitBreaker")))
						.uri("lb://TERMS-SERVER"))
				.build();
	}

}
