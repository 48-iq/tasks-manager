package dev.ivanov.tasks_manager.group_service.dto;

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
    private List<String> userIds;

    public static GroupDto from(Group group) {
        var formatter = DateTimeFormatter.ofPattern("ss:mm:HH/dd.MM.yyyy");
        return GroupDto.builder()
                .id(group.getId())
                .createdAt(group.getCreatedAt().format(formatter))
                .title(group.getTitle())
                .description(group.getDescription())
                .build();
    }
}
