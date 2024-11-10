package dev.ivanov.tasks_manager.group_service.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCreateDto {
    private String title;
    private String description;
}
