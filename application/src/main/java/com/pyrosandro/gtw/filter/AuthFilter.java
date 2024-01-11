package com.pyrosandro.gtw.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pyrosandro.common.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthFilter implements GatewayFilter {


    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final WebClient webClient;
    private final DiscoveryClient discoveryClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(exchange.getRequest().getHeaders().get(AUTHORIZATION_HEADER) == null) {
            ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST, "Error - Missing Authorization header");
            return createErrorResponse(exchange, errorDTO);
        }
        String jwtToken = exchange.getRequest().getHeaders().get(AUTHORIZATION_HEADER).get(0);

        String resourcePath = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_PREDICATE_MATCHED_PATH_ATTR);
        String resourceIdentifier = resourcePath.replaceAll("\\{[^}]*\\}", "*");

        ServiceInstance serviceInstance = discoveryClient.getInstances("AUTH-SERVICE")
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No instances available for service: " + "AUTH-SERVICE"));

        String targetUri = serviceInstance.getUri().toString() + "/auth/api/auth/authorize-resource";
        return webClient.get()
                .uri(targetUri)
                .header("Authorization", jwtToken)
                .header("Resource-Identifier", resourceIdentifier)
                .exchangeToMono(response -> {
                    if(response.statusCode().isError()) {
                        return response.bodyToMono(ErrorDTO.class)
                                .flatMap(errorDTO -> createErrorResponse(exchange, errorDTO));
                    } else {
                        return chain.filter(exchange);
                    }
                });
    }

    private Mono<Void> createErrorResponse(ServerWebExchange exchange, ErrorDTO errorDTO) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        serverHttpResponse.setStatusCode(errorDTO.getHttpStatus());
        DataBuffer dataBuffer;
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            dataBuffer = serverHttpResponse.bufferFactory().wrap(om.writeValueAsBytes(errorDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return serverHttpResponse.writeWith(Mono.just(dataBuffer));
    }
}
