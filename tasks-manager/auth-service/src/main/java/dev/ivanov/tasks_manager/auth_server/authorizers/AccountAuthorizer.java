package dev.ivanov.tasks_manager.auth_server.authorizers;


import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AccountAuthorizer implements AuthorizationManager<RequestAuthorizationContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(AccountAuthorizer.class);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext requestAuthorizationContext) {

        var authentication = authenticationSupplier.get();
        var request = requestAuthorizationContext.getRequest();
        var variables = requestAuthorizationContext.getVariables();

        if (authentication == null || !authentication.isAuthenticated())
            return new AuthorizationDecision(false);
        var accountId = variables.get("accountId");
        if (accountId == null) {
            LOGGER.error("missing id param");
            return new AuthorizationDecision(false);
        }
        var jwtAuthentication = (JwtAuthenticationToken) authentication;
        var id = jwtAuthentication.getId();
        return new AuthorizationDecision(accountId.equals(id));
    }
}
