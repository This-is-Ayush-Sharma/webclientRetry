package com.example.reactibewebclinent;

import io.netty.channel.ConnectTimeoutException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.PrematureCloseException;

public class WebClientRetry {
    public static boolean shouldRetryOrNot(Throwable error){
        return error instanceof PrematureCloseException || error instanceof ConnectTimeoutException ||  error instanceof RuntimeException;
    }
}
