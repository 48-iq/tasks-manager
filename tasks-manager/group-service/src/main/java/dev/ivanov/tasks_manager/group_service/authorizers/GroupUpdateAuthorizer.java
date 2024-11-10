package dev.ivanov.tasks_manager.group_service.authorizers;

import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import dev.ivanov.tasks_manager.group_service.services.UserGroupRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class GroupUpdateAuthorizer implements AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private UserGroupRelationService userGroupRelationService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        var authentication = authenticationSupplier.get();
        var request = requestAuthorizationContext.getRequest();
        var variables = requestAuthorizationContext.getVariables();

        if (authentication == null || !authentication.isAuthenticated())
            return new AuthorizationDecision(false);
        var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        var userId = jwtAuthenticationToken.getId();
        var groupId = variables.get("groupId");
        var role = userGroupRelationService.getUserGroupRole(userId, groupId);
        if (role == null)
            return new AuthorizationDecision(false);
        var result = role.getAuthorities().stream().anyMatch(a -> a.getName().equals("UPDATE_GROUP"));
        return new AuthorizationDecision(result);
    }
}