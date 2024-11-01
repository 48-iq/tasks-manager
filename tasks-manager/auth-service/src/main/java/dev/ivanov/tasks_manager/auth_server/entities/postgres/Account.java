package dev.ivanov.tasks_manager.auth_server.entities.postgres;


import dev.ivanov.tasks_manager.auth_server.entities.redis.AccountCache;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Account {
    @Id
    private String id;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "accounts_roles",
    joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"))
    private List<Role> roles;

    public static Account from(AccountCache accountCache) {
        return Account.builder()
                .id(accountCache.getId())
                .username(accountCache.getUsername())
                .password(accountCache.getPassword())
                .build();
    }
}
