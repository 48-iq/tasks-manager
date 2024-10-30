package dev.ivanov.tasks_manager.auth_server.services;

import dev.ivanov.tasks_manager.auth_server.repositories.redis.TokenRepository;
import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistJwtCheckServiceImpl implements BlackListJwtCheckService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public boolean isOnBlacklist(String jwt) {
        return tokenRepository.existsById(jwt);
    }
}
