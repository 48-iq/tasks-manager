package dev.ivanov.tasks_manager.user_service.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtAuthenticationToken implements Authentication {
    private UserDetails userDetails;
    private boolean isAuth;

    public JwtAuthenticationToken(UserDetails userDetails) {
        this.userDetails = userDetails;
        this.isAuth = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuth;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        isAuth = isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
