package com.example.reactibewebclinent;
import com.example.reactibewebclinent.BaseWebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean("test")
    public WebClient webClientBuilder() {

        return BaseWebClient.builder().baseUrl("http://localhost:8081").build();
        //return WebClient.builder();
        //return (WebClient) new BaseWebClientBuilder().baseUrl("http://localhost:8080").build();
    }

}