package dev.ivanov.tasks_manager.group_service.repositories.postgres;

import dev.ivanov.tasks_manager.group_service.entities.postgres.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

    @Query("select t from Topic t join t.group g where g.id = :groupId")
    List<Topic> findTopicsByGroup(String groupId);
}
