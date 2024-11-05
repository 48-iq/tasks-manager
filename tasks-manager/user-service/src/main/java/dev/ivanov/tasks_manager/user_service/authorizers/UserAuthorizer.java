package dev.ivanov.tasks_manager.user_service.authorizers;

import dev.ivanov.tasks_manager.core.authorization.Authorizer;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserAuthorizer implements Authorizer {
    @Override
    public boolean authorize(Authentication authentication,
                             HttpServletRequest request,
                             Map<String, String> variables) {
        if (authentication == null || !authentication.isAuthenticated())
            return false;
        var userId = variables.get("userId");
        if (userId == null)
            return false;
        var jwtAuthentication = (JwtAuthenticationToken) authentication;
        return jwtAuthentication.getId().equals(userId);
    }
}
