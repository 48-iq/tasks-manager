package dev.ivanov.tasks_manager.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class JwtAuthenticationToken implements Authentication {
    private boolean isAuthenticated;
    private String jwt;
    private String username;
    private List<String> roles;
    private String id;
    private JwtUserDetails details;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }
}
