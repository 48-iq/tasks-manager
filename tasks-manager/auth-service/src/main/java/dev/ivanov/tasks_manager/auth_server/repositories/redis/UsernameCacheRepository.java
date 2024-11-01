package dev.ivanov.tasks_manager.auth_server.repositories.redis;

import dev.ivanov.tasks_manager.auth_server.entities.redis.UsernameCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernameCacheRepository extends CrudRepository<UsernameCache, String> {
}
