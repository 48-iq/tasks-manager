package dev.ivanov.tasks_manager.auth_server.validators;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.ivanov.tasks_manager.auth_server.dto.RefreshDto;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RefreshDtoValidator implements Validator {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return RefreshDto.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        var refreshDto = (RefreshDto) target;
        var refresh = refreshDto.getRefresh();
        try {
            jwtUtils.verifyRefresh(refresh);
        } catch (JWTVerificationException e) {
            errors.reject("refresh", "incorrect refresh");
        }
    }
}
