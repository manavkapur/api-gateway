package com.supremesolutions.api_gateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class SecureHttpClientConfig {

    /**
     * ✅ Ensures HTTPS communication for all outbound requests (Cloud Run → Cloud Run)
     */
    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector() throws SSLException {
        HttpClient httpClient = HttpClient.create()
                .secure(ssl -> {
                    try {
                        ssl.sslContext(SslContextBuilder.forClient().build());
                    } catch (SSLException e) {
                        throw new RuntimeException("Failed to configure SSL", e);
                    }
                });
        return new ReactorClientHttpConnector(httpClient);
    }

    /**
     * ✅ Global WebClient bean used internally by Spring Cloud Gateway
     */
    @Bean
    public WebClient webClient(ReactorClientHttpConnector connector) {
        return WebClient.builder()
                .clientConnector(connector)
                .build();
    }
}
