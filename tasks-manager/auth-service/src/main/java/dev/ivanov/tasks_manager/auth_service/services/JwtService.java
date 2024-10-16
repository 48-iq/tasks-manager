package dev.ivanov.tasks_manager.auth_service.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.ivanov.tasks_manager.auth_service.entities.UserCred;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Integer expiration;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.subject}")
    private String subject;

    public String generate(UserCred user) {
        var expirationDate = ZonedDateTime.now()
                .plusSeconds(expiration)
                .toInstant();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withExpiresAt(expirationDate)
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().name())
                .sign(Algorithm.HMAC256(secret));
    }
}
