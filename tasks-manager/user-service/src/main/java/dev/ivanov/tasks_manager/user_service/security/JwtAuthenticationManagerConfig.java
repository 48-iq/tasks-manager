package dev.ivanov.tasks_manager.user_service.security;

import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class JwtAuthenticationManagerConfig {
    @Value("${app.jwt.issuer}")
    private String issuer;

    @Autowired
    private BlackListJwtCheckService blackListJwtCheckService;

    @Value("${app.jwt.subject}")
    private String subject;

    @Value("${app.jwt.secret-access}")
    private String secret;

    @Bean
    public AuthenticationManager jwtAuthenticationManager() {
        var provider = new JwtAuthenticationProvider();
        provider.setIssuer(issuer);
        provider.setSubject(subject);
        provider.setSecret(secret);
        provider.setBlackListJwtCheckService(blackListJwtCheckService);
        return new ProviderManager(provider);
    }
}
