package com.supremesolutions.api_gateway.config;


import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class NettyHttpClientConfig {

    /**
     * ✅ This ensures Gateway uses HTTPS/TLS for all outgoing calls
     * (Cloud Run → Cloud Run)
     */
    @Bean
    public HttpClientCustomizer secureHttpClientCustomizer() throws SSLException {
        return httpClient -> httpClient.secure(ssl -> {
            try {
                ssl.sslContext(SslContextBuilder.forClient().build());
            } catch (SSLException e) {
                throw new RuntimeException("Failed to configure SSL", e);
            }
        });
    }
}
