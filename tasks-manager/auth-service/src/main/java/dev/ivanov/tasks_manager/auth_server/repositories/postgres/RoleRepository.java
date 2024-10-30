package dev.ivanov.tasks_manager.auth_server.repositories.postgres;

import dev.ivanov.tasks_manager.auth_server.entities.postgres.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
