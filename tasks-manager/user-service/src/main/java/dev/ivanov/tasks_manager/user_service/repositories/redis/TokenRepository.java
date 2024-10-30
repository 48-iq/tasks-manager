package dev.ivanov.tasks_manager.user_service.repositories.redis;

import dev.ivanov.tasks_manager.user_service.entities.redis.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
