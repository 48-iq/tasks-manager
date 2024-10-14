package dev.ivanov.tasks_manager.auth_service.services;

import dev.ivanov.tasks_manager.auth_service.dto.SignInRequest;
import dev.ivanov.tasks_manager.auth_service.exceptions.EntityNotFoundException;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public String signIn(SignInRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(token);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("user with username " +
                        request.getUsername() + " not found"));
        return jwtService.generate(user);
    }

}
