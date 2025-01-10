package com.example.gateway_server.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AddUsernameHeaderGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AddUsernameHeaderGatewayFilterFactory.Config> {

    public AddUsernameHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) ->
                ReactiveSecurityContextHolder.getContext()
                        .map(ctx -> ctx.getAuthentication())
                        .flatMap(authentication -> {
                            String username = extractUsername(authentication);
                            ServerHttpRequest request = exchange.getRequest().mutate()
                                    .header("X-Authenticated-User", username)
                                    .build();
                            return chain.filter(exchange.mutate().request(request).build());
                        });
    }

    private String extractUsername(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("preferred_username");
        }
        return "anonymous";
    }

    public static class Config {
    }
}
