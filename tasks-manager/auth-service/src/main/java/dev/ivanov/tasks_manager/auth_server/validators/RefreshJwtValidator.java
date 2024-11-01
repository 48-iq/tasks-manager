package dev.ivanov.tasks_manager.auth_server.validators;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RefreshJwtValidator implements Validator {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        try {
            var refresh = (String) target;
            var claims = jwtUtils.verifyRefresh(refresh);
            var refreshUsername = claims.get("username").asString();
            var accessUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!accessUsername.equals(refreshUsername))
                errors.reject("username", "different access and refresh username");
        } catch (JWTVerificationException e) {
            errors.reject("refresh", "invalid refresh token");
        }
    }
}
