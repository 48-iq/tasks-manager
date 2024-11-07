package dev.ivanov.tasks_manager.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class JwtUserDetails implements UserDetails {
    private String username;
    private List<GrantedAuthority> authorities;

    public JwtUserDetails(String username, List<String> authorities, List<String> roles) {
        this.username = username;
        this.authorities = new ArrayList<>();
        for (var authority: authorities) {
            var grantedAuthority = new SimpleGrantedAuthority(authority);
            this.authorities.add(grantedAuthority);
            //todo add roles to authorities list
        }
        for (var authority: roles) {
            var grantedAuthority = new SimpleGrantedAuthority(authority);
            this.authorities.add(grantedAuthority);
            //todo add roles to authorities list
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
