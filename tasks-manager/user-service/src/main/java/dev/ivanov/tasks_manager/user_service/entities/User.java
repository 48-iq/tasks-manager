package dev.ivanov.tasks_manager.user_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;
    @Column(unique = true)
    private String username;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;

}
