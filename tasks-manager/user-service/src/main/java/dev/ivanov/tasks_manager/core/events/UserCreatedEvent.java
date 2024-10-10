package dev.ivanov.tasks_manager.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEvent {
    private String id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private String role;
    private String adminPassword;
}
