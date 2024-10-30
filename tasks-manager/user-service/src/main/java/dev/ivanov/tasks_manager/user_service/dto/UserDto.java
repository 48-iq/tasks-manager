package dev.ivanov.tasks_manager.user_service.dto;

import dev.ivanov.tasks_manager.user_service.entities.postgres.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private String id;
    private String nickname;
    private String name;
    private String surname;
    private String email;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }
}
