package dev.ivanov.tasks_manager.auth_server.entities.postgres;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Role {
    @Id
    @Column(unique = true)
    private String name;
}
