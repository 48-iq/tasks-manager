package dev.ivanov.tasks_manager.group_service.repositories.postgres;

import dev.ivanov.tasks_manager.group_service.entities.postgres.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

}
