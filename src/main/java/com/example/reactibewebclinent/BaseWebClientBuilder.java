package com.example.reactibewebclinent;

import io.netty.channel.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;
import reactor.util.retry.Retry;

import java.time.Duration;


public class BaseWebClientBuilder {
    private final WebClient.Builder webClientBase;
    private final int maxRetries = 3;
    private final Duration backoffDuration = Duration.ofSeconds(2);

    public BaseWebClientBuilder() {
        this.webClientBase = WebClient.builder();
    }
    public BaseWebClientBuilder baseUrl(final String baseUrl) {
        this.webClientBase.baseUrl(baseUrl);
        return this;
    }
    public BaseWebClientBuilder headers(String headerName, String headerValue) {
        this.webClientBase.defaultHeader(headerName, headerValue);
        return this;
    }

    public BaseWebClientBuilder filters(ExchangeFilterFunction filters) {
        this.webClientBase.filter(filters);
        return this;
    }

    public WebClient build() {
        //return this.webClientBase.build();
        return this.webClientBase
                .filter((request, next) -> next.exchange(request)
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                return Mono.error(new RuntimeException("Server Error1"));
                            }
                            return Mono.just(clientResponse);
                        })
                        .retryWhen(Retry.backoff(maxRetries, backoffDuration).filter(WebClientRetry::shouldRetryOrNot).doAfterRetry(rs -> {
                            if (rs.totalRetries() >= 3) {
                                System.out.println("Retries exhausted!");
                            }
                        }))
                )
                .build();
    }
}
