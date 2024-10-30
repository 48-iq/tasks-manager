package dev.ivanov.tasks_manager.auth_server.repositories.redis;

import dev.ivanov.tasks_manager.auth_server.entities.redis.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
