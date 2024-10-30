package dev.ivanov.tasks_manager.user_service.security;

import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import dev.ivanov.tasks_manager.user_service.repositories.redis.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListJwtCheckServiceImpl implements BlackListJwtCheckService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public boolean isOnBlacklist(String jwt) {
        return tokenRepository.existsById(jwt);
    }
}
