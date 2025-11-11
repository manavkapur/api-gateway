package com.supremesolutions.api_gateway.config;

import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import io.netty.handler.ssl.SslContextBuilder;

@Configuration
public class SslWebClientConfig {

    @Bean
    public WebClient webClient() throws SSLException {
        SslProvider sslProvider = SslProvider.builder()
                .sslContext(SslContextBuilder.forClient().build())
                .build();

        HttpClient httpClient = HttpClient.create().secure(sslProvider);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
