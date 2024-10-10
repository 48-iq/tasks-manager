package dev.ivanov.tasks_manager.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserSignUpDto {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String adminPassword;
}
