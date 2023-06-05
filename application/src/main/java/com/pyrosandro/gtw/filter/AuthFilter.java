package com.pyrosandro.gtw.filter;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@AllArgsConstructor
public class AuthFilter implements GatewayFilter {


    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final WebClient webClient;
    private final DiscoveryClient discoveryClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Extract necessary information from the incoming request
        String jwtToken = extractJwtToken(exchange.getRequest());
        String resourcePath = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_PREDICATE_MATCHED_PATH_ATTR);
        String resourceIdentifier = resourcePath.replaceAll("\\{[^}]*\\}", "*");

        ServiceInstance serviceInstance = discoveryClient.getInstances("AUTH-SERVICE")
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No instances available for service: " + "AUTH-SERVICE"));

        // Forward the request to the resolved service instance
        String targetUri = serviceInstance.getUri().toString() + "/auth/api/auth/authorize-resource";


        // Make an HTTP request to your authorization microservice
        return webClient.get()
                .uri(targetUri)
                .header("Authorization", jwtToken)
                .header("Resource-Identifier", resourceIdentifier)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        // Authorization successful, allow the request to proceed
                        return chain.filter(exchange);
                    } else {
                        // Authorization denied, return appropriate response
                        exchange.getResponse().setStatusCode(response.getStatusCode());
                        return exchange.getResponse().setComplete();
                    }
                });

    }

    // Utility methods for extracting JWT token and resource identifier

    private String extractJwtToken(ServerHttpRequest request) {
        // Extract JWT token from request headers or query parameters
        return request.getHeaders().get(AUTHORIZATION_HEADER).get(0);
    }

    private String extractResourceIdentifier(ServerHttpRequest request) {
        // Extract resource identifier from request headers or path
        return request.getPath().toString();
    }
}
