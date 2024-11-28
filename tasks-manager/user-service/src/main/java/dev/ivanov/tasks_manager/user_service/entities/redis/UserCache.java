package dev.ivanov.tasks_manager.user_service.entities.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCache {
    @Id
    private String id;
    private String nickname;
    private String name;
    private String surname;
    private String email;
}
