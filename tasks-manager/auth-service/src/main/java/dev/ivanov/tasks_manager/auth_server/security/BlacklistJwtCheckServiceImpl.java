package dev.ivanov.tasks_manager.auth_server.security;

import dev.ivanov.tasks_manager.auth_server.repositories.redis.BlacklistTokenRepository;
import dev.ivanov.tasks_manager.core.security.BlackListJwtCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistJwtCheckServiceImpl implements BlackListJwtCheckService {
    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Override
    public boolean isOnBlacklist(String jwt) {
        return blacklistTokenRepository.existsById(jwt);
    }
}
