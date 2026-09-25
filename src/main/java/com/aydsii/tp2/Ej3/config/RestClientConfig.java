package com.aydsii.tp2.Ej3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient frankfurterRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.frankfurter.dev/v2")
                .build();
    }
}