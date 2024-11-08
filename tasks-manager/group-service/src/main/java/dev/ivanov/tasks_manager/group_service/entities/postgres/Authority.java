package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority {
    private String name;
}
