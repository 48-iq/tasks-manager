package dev.ivanov.tasks_manager.auth_service.controllers;

import dev.ivanov.tasks_manager.auth_service.dto.JwtResponse;
import dev.ivanov.tasks_manager.auth_service.dto.SignInRequest;
import dev.ivanov.tasks_manager.auth_service.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            var jwt = authService.signIn(request);
            var jwtResponse = JwtResponse.builder()
                    .token(jwt)
                    .build();
            return ResponseEntity.ok(jwtResponse);
        } catch (AuthenticationException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("authentication exception");
        }
    }

}
