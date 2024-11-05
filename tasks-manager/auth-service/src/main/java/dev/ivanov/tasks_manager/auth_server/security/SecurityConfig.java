package dev.ivanov.tasks_manager.auth_server.security;

import dev.ivanov.tasks_manager.auth_server.authorizers.AccountAuthorizer;
import dev.ivanov.tasks_manager.core.authorization.ResourceAuthorizationManager;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationProvider;
import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AccountAuthorizer accountAuthorizer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/change-password").access(resourceAuthorizationManager())
                        .requestMatchers("/api/auth/delete-account/**").access(resourceAuthorizationManager())
                        .anyRequest().permitAll()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager daoAuthenticationManager() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public ResourceAuthorizationManager resourceAuthorizationManager() {
        ResourceAuthorizationManager resourceAuthorizationManager = new ResourceAuthorizationManager();
        resourceAuthorizationManager.addAuthorizer(accountAuthorizer, "/delete-account/{accountId}", "DELETE");
        resourceAuthorizationManager.addAuthorizer(accountAuthorizer, "/change-password/{accountId}", "PUT");
        resourceAuthorizationManager.addAuthorizer(accountAuthorizer, "/refresh/{accountId}", "POST");
        return resourceAuthorizationManager;
    }

}
