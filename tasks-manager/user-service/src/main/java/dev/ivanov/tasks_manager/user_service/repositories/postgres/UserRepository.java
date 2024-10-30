package dev.ivanov.tasks_manager.user_service.repositories.postgres;

import dev.ivanov.tasks_manager.user_service.entities.postgres.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
