package com.example.reactibewebclinent;


import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.codec.PrematureChannelClosureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@Import(WebClientConfig.class)
public class ApiController {

    private final WebClient webClient;

    @Autowired
    public ApiController(@Qualifier("test") WebClient webClientBuilder) {
        this.webClient = webClientBuilder;
    }

    private Boolean retryLogic(Throwable error){
        System.out.println("Retry Done");
        return error instanceof RuntimeException;
    }

    @GetMapping("/hello")
    public Mono<String> getHelloFromServer() {
        return webClient.get()
                .uri("/slow-endpoint")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/")
    public Mono<String> index() {
        //return Flux.just("Hello World!", "wold");
        return Mono.just("Hello World");
        //return Mono.error(new RuntimeException("An error occurred!"));
        //return Mono.error(new InterruptedException());

        //return Mono.error(new RuntimeException("Something went wrong!"));
        //return Mono.error(new ConnectTimeoutException("Connection timed out"));
        //return Mono.error(new PrematureChannelClosureException());
        //return Mono.error(new ArithmeticException());
    }
}
