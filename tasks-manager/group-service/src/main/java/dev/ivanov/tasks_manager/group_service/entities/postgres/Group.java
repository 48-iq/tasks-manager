package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Group {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
