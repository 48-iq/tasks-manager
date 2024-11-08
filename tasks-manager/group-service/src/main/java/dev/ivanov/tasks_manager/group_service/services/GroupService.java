package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.dto.GroupCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.GroupDto;
import dev.ivanov.tasks_manager.group_service.entities.postgres.Group;
import dev.ivanov.tasks_manager.group_service.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private RestTemplate restTemplate;


    public GroupDto createGroup(String userId, GroupCreateDto groupCreateDto) {
        var uuidResponseEntity = restTemplate.getForEntity(gatewayHost + "/api/uuid", String.class);
        if (uuidResponseEntity.getStatusCode().isError())
            throw new InternalServerException("uuid service is not available");
        var id = uuidResponseEntity.getBody();
        var group = Group.builder()
                .id(id)
                .title(groupCreateDto.getTitle())
                .description(groupCreateDto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        var savedGroup = groupRepository.save(group);
        return GroupDto
    }
}
