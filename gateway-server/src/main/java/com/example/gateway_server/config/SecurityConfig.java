package com.example.gateway_server.config;

import com.example.gateway_server.util.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

import static com.example.gateway_server.constant.LoanUri.*;
import static com.example.gateway_server.constant.Role.CUSTOMER;
import static com.example.gateway_server.constant.Role.MANAGER;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/loan/accept-terms/**").authenticated()
                                .pathMatchers(HttpMethod.POST, APPLICATION.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.GET, APPLICATION.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.PUT, APPLICATION.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.DELETE, APPLICATION.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.GET, BALANCE.getUri()).authenticated()
                                .pathMatchers(HttpMethod.DELETE, BALANCE.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.POST, COUNSEL.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.GET, COUNSEL.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.PUT, COUNSEL.getUri()).hasAnyRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.PATCH, COUNSEL.getUri()).hasAnyRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.DELETE, COUNSEL.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(ENTRY.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.POST, FILE_STORAGE.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.GET, FILE_STORAGE.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.DELETE, FILE_STORAGE.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.GET, JUDGEMENT.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.POST, JUDGEMENT.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.PUT, JUDGEMENT.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.DELETE, JUDGEMENT.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.PATCH, JUDGEMENT.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.POST, REPAYMENT.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.GET, REPAYMENT.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.PUT, REPAYMENT.getUri()).hasRole(CUSTOMER.getRole())
                                .pathMatchers(HttpMethod.DELETE, REPAYMENT.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.GET, TERMS.getUri()).hasAnyRole(CUSTOMER.getRole(), MANAGER.getRole())
                                .pathMatchers(HttpMethod.POST, TERMS.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.PUT, TERMS.getUri()).hasRole(MANAGER.getRole())
                                .pathMatchers(HttpMethod.DELETE, TERMS.getUri()).hasRole(MANAGER.getRole())
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        serverHttpSecurity.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()));
        return serverHttpSecurity.build();
    }



    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "DELETE",
                "PUT",
                "PATCH"
        ));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}