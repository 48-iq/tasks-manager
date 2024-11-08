package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_groups")
@Builder
@Getter
@Setter
public class UserGroupRelation {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "group_role_name", referencedColumnName = "name")
    private GroupRole groupRole;
}
