package dev.ivanov.tasks_manager.auth_server.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.ivanov.tasks_manager.auth_server.exceptions.AuthorizationException;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import dev.ivanov.tasks_manager.core.security.exceptions.BlacklistJwtAuthorizationException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("in filter");

        try {
            var authorizationHeader = request.getHeader("authorization");
            if (authorizationHeader != null) {
                if (!authorizationHeader.startsWith("Bearer "))
                    throw new AuthorizationException();
                var jwt = authorizationHeader.substring(7);
                var token = new JwtAuthenticationToken();
                var claims = jwtUtils.verifyAccess(jwt);
                var id = claims.get("id").asString();
                var username = claims.get("username").asString();
                var roles = claims.get("roles").asList(String.class);
                token.setRoles(roles);
                token.setId(id);
                token.setUsername(username);
                token.setJwt(jwt);
                token.setAuthenticated(true);
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthorizationException | JWTVerificationException | AuthenticationException | BlacklistJwtAuthorizationException e) {
            LOGGER.error(e.getMessage());
            response.getWriter().write("authentication error");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
