package dev.ivanov.tasks_manager.group_service.dto.topic;

import dev.ivanov.tasks_manager.group_service.entities.postgres.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicCreateDto {
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private User creator;
    private Integer complexity;
    private Integer importance;
    private String theme;
}
