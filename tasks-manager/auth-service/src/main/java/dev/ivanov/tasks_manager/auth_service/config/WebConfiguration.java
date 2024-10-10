package dev.ivanov.tasks_manager.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfiguration {
    @Value("$app.gateway.service-secret")
    private String serviceSecret;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
        return restTemplateBuilder
                .defaultHeader("x-service-secret", serviceSecret)
                .build();
    }
}
