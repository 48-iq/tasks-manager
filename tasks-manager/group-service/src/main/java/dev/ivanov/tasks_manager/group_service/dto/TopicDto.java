package dev.ivanov.tasks_manager.group_service.dto;

import dev.ivanov.tasks_manager.core.utils.Formatters;
import dev.ivanov.tasks_manager.group_service.entities.postgres.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDto {
    private String id;
    private String title;
    private String description;
    private String createdAt;
    private String creatorId;
    private Integer complexity;
    private Integer importance;
    private String theme;

    public static TopicDto from(Topic topic) {
        return TopicDto.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .createdAt(topic.getCreatedAt().format(Formatters.DATE_TIME_FORMATTER))
                .creatorId(topic.getCreator().getId())
                .complexity(topic.getComplexity())
                .importance(topic.getImportance())
                .theme(topic.getTheme())
                .build();
    }
}
