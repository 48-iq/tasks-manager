package dev.ivanov.tasks_manager.auth_service.services;

import dev.ivanov.tasks_manager.auth_service.dto.JwtResponse;
import dev.ivanov.tasks_manager.auth_service.dto.NewUserRequest;
import dev.ivanov.tasks_manager.auth_service.dto.SignInRequest;
import dev.ivanov.tasks_manager.auth_service.dto.SignUpRequest;
import dev.ivanov.tasks_manager.auth_service.entities.Role;
import dev.ivanov.tasks_manager.auth_service.entities.User;
import dev.ivanov.tasks_manager.auth_service.exceptions.EntityNotFoundException;
import dev.ivanov.tasks_manager.auth_service.exceptions.SignUpException;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${app.gateway.host}")
    private String gatewayHost;


    public String signIn(SignInRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(token);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("user with username " +
                        request.getUsername() + " not found"));
        return jwtService.generate(user);
    }
}
