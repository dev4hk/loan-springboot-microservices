package com.example.gateway_server.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BlockAccessFilter extends AbstractGatewayFilterFactory<BlockAccessFilter.Config> {

    public BlockAccessFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            HttpMethod method = exchange.getRequest().getMethod();

            if (path.contains("/ACCEPT-TERMS-SERVER")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            if (path.contains("/BALANCE-SERVER") || path.contains("/loan/balance")) {
                if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
