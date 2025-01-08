package com.example.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/loan/accept-terms/**").authenticated()
                        .pathMatchers("/loan/application/**").authenticated()
                        .pathMatchers("/loan/balance/**").authenticated()
                        .pathMatchers("/loan/counsel/**").authenticated()
                        .pathMatchers("/loan/entry/**").authenticated()
                        .pathMatchers("/loan/file-storage/**").authenticated()
                        .pathMatchers("/loan/judgement/**").authenticated()
                        .pathMatchers("/loan/repayment/**").authenticated()
                        .pathMatchers("/loan/terms/**").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(Customizer.withDefaults()));
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }
}