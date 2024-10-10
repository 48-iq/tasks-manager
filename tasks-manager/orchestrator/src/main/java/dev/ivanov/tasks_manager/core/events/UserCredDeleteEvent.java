package dev.ivanov.tasks_manager.core.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCredDeleteEvent {
    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
}
