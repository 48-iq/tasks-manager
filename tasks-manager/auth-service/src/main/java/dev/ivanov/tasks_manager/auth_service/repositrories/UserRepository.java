package dev.ivanov.tasks_manager.auth_service.repositrories;

import dev.ivanov.tasks_manager.auth_service.entities.UserCred;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserCred, String> {
    Optional<UserCred> findByUsername(String username);
    boolean existsByUsername(String username);
}
