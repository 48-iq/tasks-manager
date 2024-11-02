package dev.ivanov.tasks_manager.auth_server.authorizers;


import dev.ivanov.tasks_manager.core.authorization.Authorizer;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccountAuthorizer implements Authorizer {
    public static final Logger LOGGER = LoggerFactory.getLogger(AccountAuthorizer.class);

    @Override
    public boolean authorize(Authentication authentication,
                             HttpServletRequest request,
                             Map<String, String> variables) {
        var accountId = variables.get("id");
        if (accountId == null) {
            LOGGER.error("missing id param");
            return false;
        }
        var jwtAuthentication = (JwtAuthenticationToken) authentication;
        var id = jwtAuthentication.getId();
        return accountId.equals(id);
    }
}
