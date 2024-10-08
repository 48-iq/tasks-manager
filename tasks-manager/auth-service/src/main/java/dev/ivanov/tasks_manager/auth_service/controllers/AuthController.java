package dev.ivanov.tasks_manager.auth_service.controllers;

import dev.ivanov.tasks_manager.auth_service.dto.JwtResponse;
import dev.ivanov.tasks_manager.auth_service.dto.SignInRequest;
import dev.ivanov.tasks_manager.auth_service.dto.SignUpRequest;
import dev.ivanov.tasks_manager.auth_service.services.AuthService;
import dev.ivanov.tasks_manager.auth_service.validators.SignUpRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private SignUpRequestValidator signUpRequestValidator;

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(SignInRequest request) {

        authService.signIn()
    }

}
