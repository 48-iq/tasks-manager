package dev.ivanov.tasks_manager.user_service.repositories.redis;

import dev.ivanov.tasks_manager.user_service.entities.redis.UserCache;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCacheRepository extends CrudRepository<UserCache, String> {
}
