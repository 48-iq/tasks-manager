package dev.ivanov.tasks_manager.group_service.controllers;

import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupDto;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupUpdateDto;
import dev.ivanov.tasks_manager.group_service.exceptions.GroupNotFoundException;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import dev.ivanov.tasks_manager.group_service.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("/by-id/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable String groupId) {
        var groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty())
            return ResponseEntity.badRequest().body("group with id " + groupId + " not found");
        return ResponseEntity.ok(GroupDto.from(groupOptional.get()));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable String userId) {
        var groups = groupService.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateDto groupCreateDto) {
        var jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var groupDto = groupService.createGroup(jwtAuthenticationToken.getId(), groupCreateDto);
        return ResponseEntity.ok(groupDto);
    }

    @PutMapping("/update-group/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable String groupId, @RequestBody GroupUpdateDto groupUpdateDto) {
        var groupDto = groupService.updateGroup(groupId, groupUpdateDto);
        return ResponseEntity.ok(groupDto);
    }

    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

}
