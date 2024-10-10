package dev.ivanov.tasks_manager.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredCreatedEvent {
    private String id;
    private String username;
    private String role;
    private boolean isError;
}
