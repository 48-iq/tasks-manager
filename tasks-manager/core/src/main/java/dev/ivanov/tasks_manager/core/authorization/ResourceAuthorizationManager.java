package dev.ivanov.tasks_manager.core.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceAuthorizationManager.class);
    private final Map<HttpTemplateSignature, Authorizer> authorizerMap = new HashMap<>(); //map <<String pattern, String method>, Authorize which use for this prefix>

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        var request = requestAuthorizationContext.getRequest();
        var variables = requestAuthorizationContext.getVariables();
        var authentication = authenticationSupplier.get();
        if (authentication == null || !authentication.isAuthenticated())
            return new AuthorizationDecision(false);
        var authorizerMapKeys = authorizerMap.keySet();
        boolean isChecked = false;
        for (var key: authorizerMapKeys) {
            if (key.matches(request.getPathInfo(), request.getMethod())) {
                var authorizer = authorizerMap.get(key);
                isChecked = true;
                if (!authorizer.authorize(authentication, request, variables)) {
                    return new AuthorizationDecision(false);
                }
            }
        }
        if (!isChecked) {
            LOGGER.info("request was not checked by Authorizer");
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }

    public void addAuthorizer(Authorizer authorizer, String template, String... methods) {
        authorizerMap.put(new HttpTemplateSignature(template, methods), authorizer);
    }
}
