package dev.ivanov.tasks_manager.auth_server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import dev.ivanov.tasks_manager.auth_server.dto.SignInDto;
import dev.ivanov.tasks_manager.auth_server.dto.TokenDto;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import dev.ivanov.tasks_manager.auth_server.exceptions.AccountNotFoundException;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager daoAuthenticationManager;

    @Autowired
    private AccountRepository accountRepository;

    public TokenDto signIn(SignInDto signInDto) {
        var authentication = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());
        daoAuthenticationManager.authenticate(authentication);
        var account = accountRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new AccountNotFoundException("account with username " +
                        signInDto.getUsername() + " not found"));
        return jwtUtils.generateToken(account);
    }


}
