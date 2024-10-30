package dev.ivanov.tasks_manager.auth_server.repositories.redis;

import dev.ivanov.tasks_manager.auth_server.entities.redis.AccountCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCacheRepository extends CrudRepository<AccountCache, String> {
}
