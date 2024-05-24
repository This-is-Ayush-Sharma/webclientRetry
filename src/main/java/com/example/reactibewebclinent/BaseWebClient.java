package com.example.reactibewebclinent;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class BaseWebClient {
    private final WebClient.Builder webClientBase;
    private final int maxRetries = 3;
    private final Duration backoffDuration = Duration.ofSeconds(2);

    public BaseWebClient(WebClient.Builder webClientBase) {
        this.webClientBase = webClientBase;
    }

    public static BaseWebClient builder() {
        return new BaseWebClient(WebClient.builder());
    }

    public BaseWebClient baseUrl(final String baseUrl) {
        this.webClientBase.baseUrl(baseUrl);
        return this;
    }
    public BaseWebClient headers(String headerName, String headerValue) {
        this.webClientBase.defaultHeader(headerName, headerValue);
        return this;
    }
    public BaseWebClient filters(ExchangeFilterFunction filters) {
        this.webClientBase.filter(filters);
        return this;
    }

    public BaseWebClient clientConnector(ReactorClientHttpConnector connector){
        this.webClientBase.clientConnector(connector);
        return this;
    }

    public ExchangeFilterFunction retry() {
        return (request, next) -> next.exchange(request)
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
                }));
    }

    public WebClient build() {
        System.out.println("enter build");
        return this.webClientBase
                .filter(retry())
                .build();
    }
}
