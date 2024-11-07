package dev.ivanov.tasks_manager.user_service.security;

import dev.ivanov.tasks_manager.core.authorization.ResourceAuthorizationManager;
import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationProvider;
import dev.ivanov.tasks_manager.user_service.authorizers.UserAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationManager jwtAuthenticationManager;

    @Autowired
    private UserAuthorizer userAuthorizer;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(HttpMethod.PUT, "/api/users/**").access(resourceAuthorizationManager())
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").access(resourceAuthorizationManager())
                                .anyRequest().permitAll())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public ResourceAuthorizationManager resourceAuthorizationManager() {
        var authorizationManager = new ResourceAuthorizationManager();
        authorizationManager.addAuthorizer(userAuthorizer, "/api/users/{userId}", "PUT");
        return authorizationManager;
    }
}
