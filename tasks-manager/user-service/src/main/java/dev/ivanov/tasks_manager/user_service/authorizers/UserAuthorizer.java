package dev.ivanov.tasks_manager.user_service.authorizers;

import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class UserAuthorizer implements AuthorizationManager<RequestAuthorizationContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizer.class);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        var authentication = authenticationSupplier.get();
        var request = requestAuthorizationContext.getRequest();
        var variables = requestAuthorizationContext.getVariables();
        if (authentication == null || !authentication.isAuthenticated())
            return new AuthorizationDecision(false);
        var userId = variables.get("userId");
        if (userId == null)
            return new AuthorizationDecision(false);
        var jwtAuthentication = (JwtAuthenticationToken) authentication;
        return new AuthorizationDecision(jwtAuthentication.getId().equals(userId));
    }
}
