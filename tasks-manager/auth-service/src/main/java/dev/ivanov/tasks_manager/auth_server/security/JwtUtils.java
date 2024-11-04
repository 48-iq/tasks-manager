package dev.ivanov.tasks_manager.auth_server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import dev.ivanov.tasks_manager.auth_server.dto.TokenDto;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Authority;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import dev.ivanov.tasks_manager.core.security.exceptions.BlacklistJwtAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret-refresh}")
    private String refreshSecret;

    @Value("${app.jwt.secret-access}")
    private String accessSecret;


    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.subject}")
    private String subject;

    @Value("${app.jwt.expiration-access}")
    private Integer expirationAccess;

    @Value("${app.jwt.expiration-refresh}")
    private Integer expirationRefresh;

    @Autowired
    private BlackListJwtCheckService blackListJwtCheckService;

    public TokenDto generateToken(Account account) {
        var access = generateAccess(account);
        var refresh = generateRefresh(account);
        return TokenDto.builder()
                .access(access)
                .refresh(refresh)
                .build();
    }

    public String generateAccess(Account account) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaim("id", account.getId())
                .withClaim("username", account.getUsername())
                .withClaim("roles", account.getRoles().stream().map(Role::getName).toList())
                .withClaim("authorities", account.getRoles()
                        .stream()
                        .flatMap(r -> r.getAuthorities().stream())
                        .distinct()
                        .map(Authority::getName)
                        .toList()
                )
                .withClaim("type", "access")
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusSeconds(expirationAccess).toInstant())
                .sign(Algorithm.HMAC256(accessSecret));
    }

    public String generateRefresh(Account account) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaim("id", account.getId())
                .withClaim("username", account.getUsername())
                .withClaim("roles", account.getRoles().stream().map(Role::getName).toList())
                .withClaim("authorities", account.getRoles()
                        .stream()
                        .flatMap(r -> r.getAuthorities().stream())
                        .distinct()
                        .map(Authority::getName)
                        .toList()
                )
                .withClaim("type", "refresh")
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusSeconds(expirationRefresh).toInstant())
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public Map<String, Claim> verifyAccess(String jwt) {
        var verifier = JWT.require(Algorithm.HMAC256(accessSecret))
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaimPresence("id")
                .withClaimPresence("username")
                .withClaimPresence("roles")
                .withClaim("type", "access")
                .build();
        if (blackListJwtCheckService.isOnBlacklist(jwt))
            throw new BlacklistJwtAuthorizationException("jwt in blacklist");
        return verifier.verify(jwt)
                .getClaims();
    }

    public Map<String, Claim> verifyRefresh(String jwt) {
        var verifier = JWT.require(Algorithm.HMAC256(refreshSecret))
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaimPresence("id")
                .withClaimPresence("username")
                .withClaimPresence("roles")
                .withClaim("type", "refresh")
                .build();
        if (blackListJwtCheckService.isOnBlacklist(jwt))
            throw new JWTVerificationException("jwt in blacklist");
        return verifier.verify(jwt)
                .getClaims();
    }
}
