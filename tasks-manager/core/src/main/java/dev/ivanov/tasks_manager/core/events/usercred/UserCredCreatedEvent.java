package dev.ivanov.tasks_manager.core.events.usercred;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredCreatedEvent {
    private String transactionId;
    private String id;
    private boolean isError;
}
