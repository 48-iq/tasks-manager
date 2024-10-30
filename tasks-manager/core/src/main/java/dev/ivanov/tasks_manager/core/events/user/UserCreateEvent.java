package dev.ivanov.tasks_manager.core.events.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCreateEvent {
    private String transactionId;
    private String id;
}
