package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.entities.postgres.Invitation;
import dev.ivanov.tasks_manager.group_service.exceptions.InvitationAlreadyExists;
import dev.ivanov.tasks_manager.group_service.exceptions.InvitationNotFoundException;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.InvitationRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserGroupRelationService userGroupRelationService;

    @Transactional
    public void inviteUser(String userId, String groupId) {
        var invitationId = invitationId(userId, groupId);
        if (invitationRepository.existsById(invitationId))
            throw new InvitationAlreadyExists("invitation for user " + userId + " from group " + groupId + " already exists");
        var invitation = Invitation.builder()
                .id(invitationId)
                .user(userRepository.getReferenceById(userId))
                .group(groupRepository.getReferenceById(groupId))
                .rejected(false)
                .build();
        invitationRepository.save(invitation);
    }

    @Transactional
    public void cancelInvitation(String userId, String groupId) {
        invitationRepository.deleteById(invitationId(userId, groupId));
    }

    @Transactional
    public void acceptInvitation(String userId, String groupId) {
        var invitationId = invitationId(userId, groupId);
        var invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("invitation with id " + invitationId + " not found"));
        userGroupRelationService.saveUserGroupRelation(userId, groupId, "ROLE_GROUP_USER");
        invitationRepository.deleteById(invitationId);
    }

    @Transactional
    public void rejectInvitation(String userId, String groupId) {
        var invitationId = invitationId(userId, groupId);
        var invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("invitation with id " + invitationId + " not found"));
        invitation.setRejected(true);
        invitationRepository.save(invitation);
    }

    private String invitationId(String userId, String groupId) {
        return userId + ":" + groupId;
    }
}
