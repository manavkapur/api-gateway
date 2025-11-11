package com.supremesolutions.api_gateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class SecureReactorClientConfig {

    /**
     * ✅ Ensures all outgoing WebClient / Gateway requests use HTTPS/TLS properly
     */
    @Bean
    public WebClient webClient() throws SSLException {
        HttpClient secureHttpClient = HttpClient.create()
                .secure(spec -> {
                    try {
                        spec.sslContext(SslContextBuilder.forClient().build());
                    } catch (SSLException e) {
                        throw new RuntimeException("Failed to configure SSL", e);
                    }
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(secureHttpClient))
                .build();
    }
}
