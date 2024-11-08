package dev.ivanov.tasks_manager.group_service.controllers;

import dev.ivanov.tasks_manager.group_service.dto.GroupCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @GetMapping("/by-id/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable String groupId) {

    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable String userId) {

    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateDto groupCreateDto) {

    }

    @PutMapping("/update-group/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable String groupId) {

    }

    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {

    }

}
