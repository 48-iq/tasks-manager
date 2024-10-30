package dev.ivanov.tasks_manager.user_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import dev.ivanov.tasks_manager.core.security.exceptions.BlacklistJwtAuthorizationException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.subject}")
    private String subject;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        try {
            var authorizationHeader = request.getHeader("authorization");
            if (authorizationHeader != null) {
                if (!authorizationHeader.startsWith("bearer "))
                    throw new JWTVerificationException("incorrect jwt format");
                var jwt = authorizationHeader.substring(7);
                var token = new JwtAuthenticationToken();
                token.setJwt(jwt);
                authenticationManager.authenticate(token);

            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException | UsernameNotFoundException | BlacklistJwtAuthorizationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("incorrect jwt");
        }
    }
}
