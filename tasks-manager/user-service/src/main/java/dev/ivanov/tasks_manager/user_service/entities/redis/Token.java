package dev.ivanov.tasks_manager.user_service.entities.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash
public class Token {
    @Id
    private String id;
    @TimeToLive
    private Long expiration;
    private String token;
}
