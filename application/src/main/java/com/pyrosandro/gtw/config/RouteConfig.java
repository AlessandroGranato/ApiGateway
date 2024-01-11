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
                //AUTH ROUTES
                .route(r -> r.path("/auth/api/auth/signup").uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/auth/signin").uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/test/all").uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/test/user")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/test/mod")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/test/admin")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/auth/refresh-token").uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/auth/authorize-resource")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/auth/api/auth/{id}")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://AUTH-SERVICE"))

                //BUGO-DATA-SHELL ROUTES
                .route(r -> r.path("/bds/api/devices/{id}")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
                .route(r -> r.path("/bds/api/devices")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
                .route(r -> r.path("/bds/api/users/{id}")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
                .route(r -> r.path("/bds/api/users")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
                .route(r -> r.path("/bds/api/temperatures/{id}")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
                .route(r -> r.path("/bds/api/temperatures")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://BDS-SERVICE"))
//                  Used to keep track of rewrite param. Should not be useful anymore
//                .route(r -> r.path("/auth/api/auth/{param1}/test/{param2}")
//                        .filters(f ->
//                                f.filter(authFilter)
//                                        .rewritePath("/auth/api/auth/(?<param1>.*)/test/(?<param2>.*)", "/auth/api/auth/${param1}/test/${param2}")
//                        )
//                        .uri("lb://AUTH-SERVICE"))
                .build();
    }
}