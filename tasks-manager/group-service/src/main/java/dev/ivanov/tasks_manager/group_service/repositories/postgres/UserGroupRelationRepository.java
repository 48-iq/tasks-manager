package dev.ivanov.tasks_manager.group_service.repositories.postgres;

import dev.ivanov.tasks_manager.group_service.entities.postgres.UserGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRelationRepository extends JpaRepository<UserGroupRelation, String> {
    @Query("select ugr from UserGroupRelation ugr join ugr.group g where g.id = :groupId")
    List<UserGroupRelation> findRelationsByGroup(String groupId);
}
