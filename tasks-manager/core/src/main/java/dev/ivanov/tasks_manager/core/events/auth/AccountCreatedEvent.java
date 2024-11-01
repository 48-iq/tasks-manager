package dev.ivanov.tasks_manager.core.events.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreatedEvent {
    //todo add timestamp to events to the skip events if it is too old
    private String transactionId;
    private Instant timestamp;
    private String id;
}
