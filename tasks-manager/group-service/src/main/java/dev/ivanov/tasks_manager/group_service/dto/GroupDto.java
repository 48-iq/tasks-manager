package dev.ivanov.tasks_manager.group_service.dto;

import dev.ivanov.tasks_manager.core.utils.Formatters;
import dev.ivanov.tasks_manager.group_service.entities.postgres.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private String id;
    private String title;
    private String description;
    private String createdAt;

    public static GroupDto from(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .createdAt(group.getCreatedAt().format(Formatters.DATE_TIME_FORMATTER))
                .title(group.getTitle())
                .description(group.getDescription())
                .build();
    }
}
