package dev.ivanov.tasks_manager.auth_server.validators;

import dev.ivanov.tasks_manager.auth_server.dto.ChangePasswordDto;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import dev.ivanov.tasks_manager.auth_server.services.AuthService;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class ChangePasswordDtoValidator implements Validator {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RefreshJwtValidator refreshJwtValidator;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return ChangePasswordDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var changePasswordDto = (ChangePasswordDto) target;
        var newPassword = changePasswordDto.getNewPassword();
        var refresh = changePasswordDto.getRefreshToken();

        refreshJwtValidator.validate(refresh, errors);
    }


}
