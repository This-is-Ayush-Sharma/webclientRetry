package com.example.reactibewebclinent;

import io.netty.channel.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.netty.http.client.PrematureCloseException;
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
                .uri("/")
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .bodyToMono(String.class);
//                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
//                        .filter(this::retryLogic).doAfterRetry(rs -> {
//                            if (rs.totalRetries() >= 3) {
//                                System.out.println("Retries exhausted!");
//                            }
//                        }));
    }

    @GetMapping("/")
    public Flux<String> index() {
        return Flux.just("Hello World!", "wold");
        //return Mono.error(new RuntimeException("An error occurred!"));
        //return Mono.error(new RuntimeException("Something went wrong!"));
        //return Mono.error(new ConnectTimeoutException("Connection timed out"));

    }
}
