package dev.ivanov.tasks_manager.group_service.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicUpdateDto {

    private String title;
    private String description;
    private Integer complexity;
    private Integer importance;
    private String theme;
}
