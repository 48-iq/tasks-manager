package dev.ivanov.tasks_manager.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import dev.ivanov.tasks_manager.core.security.exceptions.BlacklistJwtAuthorizationException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    public static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private String secret;
    private String issuer;
    private String subject;
    private BlackListJwtCheckService blackListJwtCheckService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        var jwt = jwtAuthenticationToken.getJwt();
        if (blackListJwtCheckService != null && blackListJwtCheckService.isOnBlacklist(jwt))
            throw new BlacklistJwtAuthorizationException("jwt" + jwt + " is on blacklist");
        var claims = verify(jwt);
        jwtAuthenticationToken.setId(claims.get("id").asString());
        jwtAuthenticationToken.setUsername(claims.get("username").asString());
        jwtAuthenticationToken.setRoles(claims.get("roles").asList(String.class));
        jwtAuthenticationToken.setAuthenticated(true);
        jwtAuthenticationToken.setDetails(new JwtUserDetails(claims.get("username").asString(),
                claims.get("authorities").asList(String.class), claims.get("roles").asList(String.class)));
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }

    private Map<String, Claim> verify(String jwt) {
        var verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaimPresence("id")
                .withClaimPresence("username")
                .withClaimPresence("roles")
                .withClaimPresence("authorities")
                .withClaim("type", "access")
                .build();
        return verifier.verify(jwt)
                .getClaims();
    }
}
