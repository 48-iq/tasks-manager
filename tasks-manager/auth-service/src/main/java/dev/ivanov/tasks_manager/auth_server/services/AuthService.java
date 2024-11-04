package dev.ivanov.tasks_manager.auth_server.services;

import dev.ivanov.tasks_manager.auth_server.dto.SignInDto;
import dev.ivanov.tasks_manager.auth_server.dto.TokenDto;
import dev.ivanov.tasks_manager.auth_server.exceptions.AccountNotFoundException;
import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import dev.ivanov.tasks_manager.auth_server.repositories.redis.BlacklistTokenRepository;
import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;


    @Transactional
    public TokenDto signIn(SignInDto signInDto) {
        var authentication = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());
        daoAuthenticationManager.authenticate(authentication);
        var account = accountRepository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new AccountNotFoundException("account with username " +
                        signInDto.getUsername() + " not found"));
        return jwtUtils.generateToken(account);
    }

    @Transactional
    public String refresh(String id) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("account with id " + id + " no found"));
        return jwtUtils.generateAccess(account);
    }


}
