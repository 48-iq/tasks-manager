package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.entities.postgres.GroupRole;
import dev.ivanov.tasks_manager.group_service.entities.postgres.UserGroupRelation;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRoleRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.UserGroupRelationRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupRelationService {

    @Autowired
    private UserGroupRelationRepository userGroupRelationRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRoleRepository groupRoleRepository;

    @Transactional
    public void saveUserGroupRelation(String userId, String groupId, String groupRole) {
        var id = userGroupRelationId(userId, groupId);
        var userGroupRelationOptional = userGroupRelationRepository.findById(id);
        if (userGroupRelationOptional.isEmpty()) {
            var relation = UserGroupRelation.builder()
                    .id(id)
                    .group(groupRepository.getReferenceById(groupId))
                    .user(userRepository.getReferenceById(userId))
                    .groupRole(groupRoleRepository.getReferenceById(groupRole))
                    .build();
            userGroupRelationRepository.save(relation);
        } else {
            var relation = userGroupRelationOptional.get();
            if (!relation.getGroupRole().getName().equals(groupRole)) {
                relation.setGroupRole(groupRoleRepository.getReferenceById(groupRole));
                userGroupRelationRepository.save(relation);
            }
        }
    }

    @Transactional
    public GroupRole getUserGroupRole(String userId, String groupId) {
        var relationOptional = userGroupRelationRepository.findById(userGroupRelationId(userId, groupId));
        return relationOptional.map(UserGroupRelation::getGroupRole).orElse(null);
    }

    @Transactional
    public List<String> getUsersByGroup(String groupId) {
        var userGroupRelations = userGroupRelationRepository.findRelationsByGroup(groupId);
        var userIds = userGroupRelations.stream().map(ugr -> ugr.getUser().getId()).toList();
        return userIds;
    }

    @Transactional
    public List<String> getGroupsByUser(String userId) {
        var userGroupRelations = userGroupRelationRepository.findRelationsByUser(userId);
        var groupIds = userGroupRelations.stream().map(ugr -> ugr.getGroup().getId()).toList();
        return groupIds;
    }

    @Transactional
    public void deleteUserGroupRelation(String userId, String groupId) {
        var id = userGroupRelationId(userId, groupId);
        userGroupRelationRepository.deleteById(id);
    }

    public String userGroupRelationId(String userId, String groupId) {
        return userId + ":" + groupId;
    }

}
