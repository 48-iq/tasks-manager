package dev.ivanov.tasks_manager.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredCreateEvent {
    private String id;
    private String username;
    private String password;
    private String role;
    private String adminPassword;
}
