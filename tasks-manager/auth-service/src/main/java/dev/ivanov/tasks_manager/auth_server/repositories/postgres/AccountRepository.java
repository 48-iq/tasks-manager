package dev.ivanov.tasks_manager.auth_server.repositories.postgres;

import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
}
