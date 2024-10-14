package dev.ivanov.tasks_manager.core.events.usercred;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCredDeleteEvent {
    private String transactionId;
    private String id;
}
