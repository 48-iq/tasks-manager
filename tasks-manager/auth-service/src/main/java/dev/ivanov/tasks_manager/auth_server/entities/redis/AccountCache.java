package dev.ivanov.tasks_manager.auth_server.entities.redis;

import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@RedisHash
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCache {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles;

    public static AccountCache from(Account account) {
        return AccountCache.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
