package dev.ivanov.tasks_manager.core.events.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupUserCreateEvent {
    private String id;
    private String transactionId;
}
