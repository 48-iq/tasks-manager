package dev.ivanov.tasks_manager.core.events.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeletedEvent {
    private String transactionId;
    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
}
