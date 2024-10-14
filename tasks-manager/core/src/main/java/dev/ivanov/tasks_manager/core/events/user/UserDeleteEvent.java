package dev.ivanov.tasks_manager.core.events.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeleteEvent {
    private String transactionId;
    private String id;
}
