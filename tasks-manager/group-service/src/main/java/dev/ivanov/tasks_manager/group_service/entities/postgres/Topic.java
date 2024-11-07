package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "topics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Topic {
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private User creator;
    private Integer complexity;
    private Integer importance;
    private String theme;
}
