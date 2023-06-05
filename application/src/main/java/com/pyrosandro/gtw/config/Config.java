package com.pyrosandro.gtw.config;

import com.pyrosandro.gtw.filter.AuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class Config {

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
