package dev.ivanov.tasks_manager.user_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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
                var claims = verify(jwt);
                var jwtUserDetails = new JwtUserDetails(claims.get("id").asString(),
                        claims.get("role").asString(),
                        claims.get("username").asString());
                authenticationManager.authenticate(new JwtAuthenticationToken(jwtUserDetails));
            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("incorrect jwt");
        }
    }

    private Map<String, Claim> verify(String jwt) {
        var verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .withSubject(subject)
                .build();
        var decodedJwt = verifier.verify(jwt);
        return decodedJwt.getClaims();
    }
}
