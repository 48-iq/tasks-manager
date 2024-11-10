package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.dto.group.GroupCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupDto;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupUpdateDto;
import dev.ivanov.tasks_manager.group_service.entities.postgres.Group;
import dev.ivanov.tasks_manager.group_service.entities.postgres.GroupRole;
import dev.ivanov.tasks_manager.group_service.exceptions.GroupNotFoundException;
import dev.ivanov.tasks_manager.group_service.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserGroupRelationService userGroupRelationService;

    @Autowired
    private TopicService topicService;


    @Transactional
    public GroupDto createGroup(String userId, GroupCreateDto groupCreateDto) {
        var uuidResponseEntity = restTemplate.getForEntity(gatewayHost + "/api/uuid", String.class);
        if (uuidResponseEntity.getStatusCode().isError())
            throw new InternalServerException("uuid service is not available");

        var groupId = uuidResponseEntity.getBody();
        var group = Group.builder()
                .id(groupId)
                .title(groupCreateDto.getTitle())
                .description(groupCreateDto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        var savedGroup = groupRepository.save(group);

        userGroupRelationService.saveUserGroupRelation(userId, groupId, GroupRole.ROLE_GROUP_ADMIN);

        var groupDto = GroupDto.from(savedGroup);
        return groupDto;
    }

    @Transactional
    public GroupDto updateGroup(String groupId, GroupUpdateDto groupUpdateDto) {
        var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("group with id " + groupId + " not found"));

        group.setTitle(groupUpdateDto.getTitle());
        group.setDescription(groupUpdateDto.getDescription());
        var updatedGroup = groupRepository.save(group);
        var groupDto = GroupDto.from(updatedGroup);
        return groupDto;
    }

    @Transactional
    public void deleteGroup(String groupId) {
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public List<GroupDto> getGroupsByUser(String userId) {
        var groups = groupRepository.findGroupsByUser(userId);
        return groups.stream().map(GroupDto::from).toList();
    }


}
