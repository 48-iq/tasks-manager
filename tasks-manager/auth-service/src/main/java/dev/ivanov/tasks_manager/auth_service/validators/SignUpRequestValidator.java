package dev.ivanov.tasks_manager.auth_service.validators;

import dev.ivanov.tasks_manager.auth_service.dto.SignUpRequest;
import dev.ivanov.tasks_manager.auth_service.entities.Role;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class SignUpRequestValidator implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Value("${app.sign-up.admin.password}")
    private String adminPassword;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return SignUpRequest.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {

    }

    public void validate() {
    }
}
