package dev.ivanov.tasks_manager.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserUpdateDto {
    private String nickname;
    private String name;
    private String surname;
    private String email;
}
