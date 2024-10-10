package dev.ivanov.tasks_manager.auth_service.services;

import dev.ivanov.tasks_manager.auth_service.dto.SignInRequest;
import dev.ivanov.tasks_manager.auth_service.entities.Role;
import dev.ivanov.tasks_manager.auth_service.entities.User;
import dev.ivanov.tasks_manager.core.events.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.auth_service.exceptions.EntityNotFoundException;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public String signIn(SignInRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(token);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("user with username " +
                        request.getUsername() + " not found"));
        return jwtService.generate(user);
    }

    public void signUp(UserCredCreateEvent event) {
        var user = User.builder()
                .id(event.getId())
                .username(event.getUsername())
                .role(Role.valueOf(event.getRole()))
                .password(passwordEncoder.encode(event.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var userCredCreatedEvent = UserCredCreatedEvent.builder()
                .id(savedUser.getId())
                .role(savedUser.getRole().name())
                .username(savedUser.getUsername())
                .isError(false)
                .build();

        kafkaTemplate.send("user-cred-created-events-topic", userCredCreatedEvent.getId(),
                userCredCreatedEvent);
    }

}
