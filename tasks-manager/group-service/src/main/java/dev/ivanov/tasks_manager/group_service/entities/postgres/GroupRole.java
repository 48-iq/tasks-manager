package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "group_roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GroupRole {
    @Id
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_roles_authorities",
    joinColumns = @JoinColumn(name = "group_role_name", referencedColumnName = "name"),
    inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name"))
    private List<Authority> authorities;
}
