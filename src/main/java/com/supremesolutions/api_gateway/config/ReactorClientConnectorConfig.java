package com.supremesolutions.api_gateway.config;

import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class ReactorClientConnectorConfig {

    /**
     * ✅ Forces Spring Cloud Gateway to use a secure Reactor Netty client
     * for all outgoing HTTPS requests (Cloud Run -> Cloud Run)
     */
    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector() throws SSLException {
        HttpClient secureHttpClient = HttpClient.create()
                .secure(spec -> {
                    try {
                        spec.sslContext(SslContextBuilder.forClient().build());
                    } catch (SSLException e) {
                        throw new RuntimeException("Failed to configure SSL", e);
                    }
                });

        return new ReactorClientHttpConnector(secureHttpClient);
    }
}
