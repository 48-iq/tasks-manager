package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    private String id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGroupRelation> userGroupRelations;
}
