package dev.ivanov.tasks_manager.user_service.authorizers;

import dev.ivanov.tasks_manager.core.authorization.Authorizer;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserAuthorizer implements Authorizer {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizer.class);
    @Override
    public boolean authorize(Authentication authentication,
                             HttpServletRequest request,
                             Map<String, String> variables) {
        LOGGER.info("in authorizer");
        if (authentication == null || !authentication.isAuthenticated())
            return false;
        var userId = variables.get("userId");
        if (userId == null)
            return false;
        var jwtAuthentication = (JwtAuthenticationToken) authentication;
        LOGGER.info("auth id {}, user id {}", jwtAuthentication.getId(), userId);
        return jwtAuthentication.getId().equals(userId);
    }
}
