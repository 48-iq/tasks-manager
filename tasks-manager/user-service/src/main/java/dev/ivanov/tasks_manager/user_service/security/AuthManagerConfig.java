package dev.ivanov.tasks_manager.user_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class AuthManagerConfig {
    @Bean
    public AuthenticationManager jwtAuthenticationManager(JwtAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }
}
