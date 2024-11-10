package dev.ivanov.tasks_manager.group_service.entities.postgres;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invitations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Invitation {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Boolean result;
}
