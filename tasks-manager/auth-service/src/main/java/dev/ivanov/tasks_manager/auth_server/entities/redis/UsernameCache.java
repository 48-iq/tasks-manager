package dev.ivanov.tasks_manager.auth_server.entities.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsernameCache {
    @Id
    private String id;
    private String accountId;
}
