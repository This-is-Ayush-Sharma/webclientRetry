package com.example.reactibewebclinent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("test")
    public WebClient webClientBuilder() {
        //return WebClient.builder();
        return (WebClient) new BaseWebClientBuilder().baseUrl("http://localhost:8080").build();
    }
}
