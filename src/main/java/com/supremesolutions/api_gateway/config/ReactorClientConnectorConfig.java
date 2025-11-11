package com.supremesolutions.api_gateway.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * ✅ Ensures Spring Cloud Gateway & WebClient use a secure HTTPS (TLS) client
 * for all outbound Cloud Run requests. Fixes "not an SSL/TLS record" issue.
 */
@Configuration
public class ReactorClientConnectorConfig {

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector() throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient().build();

        HttpClient secureHttpClient = HttpClient.create()
                .secure(ssl -> ssl.sslContext(sslContext));

        return new ReactorClientHttpConnector(secureHttpClient);
    }

    /**
     * ✅ Optional: provide a secure WebClient (used internally by Gateway)
     * Ensures both manual and automatic service calls use HTTPS.
     */
    @Bean
    public WebClient secureWebClient(ReactorClientHttpConnector connector) {
        return WebClient.builder()
                .clientConnector(connector)
                .build();
    }
}
