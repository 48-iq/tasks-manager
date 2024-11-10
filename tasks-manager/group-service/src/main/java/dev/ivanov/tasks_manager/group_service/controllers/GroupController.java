package dev.ivanov.tasks_manager.group_service.controllers;

import dev.ivanov.tasks_manager.core.security.JwtAuthenticationToken;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.group.GroupUpdateDto;
import dev.ivanov.tasks_manager.group_service.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/by-id/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable String groupId) {

    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable String userId) {

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
