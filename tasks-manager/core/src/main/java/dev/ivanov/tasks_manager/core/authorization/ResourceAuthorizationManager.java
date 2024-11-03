package dev.ivanov.tasks_manager.core.authorization;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
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
        for (var key: authorizerMapKeys) {
            if (key.matches(request.getPathInfo(), request.getMethod())) {
                var authorizer = authorizerMap.get(key);
                if (!authorizer.authorize(authentication, request, variables)) {
                    return new AuthorizationDecision(false);
                }
            }
        }
        return new AuthorizationDecision(true);
    }

    public void addAuthorizer(Authorizer authorizer, String template, String... methods) {
        authorizerMap.put(new HttpTemplateSignature(template, methods), authorizer);
    }
}
