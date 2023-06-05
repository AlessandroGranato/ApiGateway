package com.pyrosandro.gtw.config;

import com.pyrosandro.gtw.filter.AuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private AuthFilter authFilter;

    public RouteConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/api/test/mod")
                        .filters(f ->
                                f.filter(authFilter)
                                        .rewritePath("/auth/api/test/(?<param>.*)", "/auth/api/test/${param}")
                        )
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/auth/{param1}/test/{param2}")
                        .filters(f ->
                                f.filter(authFilter)
                                        .rewritePath("/auth/api/auth/(?<param1>.*)/test/(?<param2>.*)", "/auth/api/auth/${param1}/test/${param2}")
                        )
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/test/{param}")
                        .filters(f ->
                                f.filter(authFilter)
                                        .rewritePath("/auth/api/test/(?<param>.*)", "/auth/api/test/${param}")
                        )
                        .uri("lb://AUTH-SERVICE"))
                .build();
    }
}
