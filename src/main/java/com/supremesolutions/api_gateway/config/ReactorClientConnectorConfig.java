package com.supremesolutions.api_gateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * ✅ Forces Spring Cloud Gateway & WebClient to use HTTPS (TLS)
 * for all outbound calls (Cloud Run → Cloud Run).
 */
@Configuration
public class ReactorClientConnectorConfig {

    @Bean
    public HttpClientCustomizer secureHttpClientCustomizer() {
        return httpClient -> httpClient.secure(ssl -> {
            try {
                ssl.sslContext(SslContextBuilder.forClient().build());
            } catch (SSLException e) {
                throw new RuntimeException("Failed to configure SSL", e);
            }
        });
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector() {
        try {
            HttpClient secureClient = HttpClient.create()
                    .secure(ssl -> ssl.sslContext(SslContextBuilder.forClient().build()));
            return new ReactorClientHttpConnector(secureClient);
        } catch (SSLException e) {
            throw new RuntimeException("Failed to configure ReactorClientHttpConnector", e);
        }
    }

    @Bean
    public WebClient secureWebClient(ReactorClientHttpConnector connector) {
        return WebClient.builder()
                .clientConnector(connector)
                .build();
    }
}
