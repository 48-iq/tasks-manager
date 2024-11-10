package dev.ivanov.tasks_manager.group_service.controllers;

import dev.ivanov.tasks_manager.group_service.dto.group.GroupCreateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @GetMapping("/by-id/{topicId}")
    public ResponseEntity<?> getGroup(@PathVariable String topicId) {

    }

    @GetMapping("/by-group/{groupId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable String groupId) {

    }

    @PostMapping("/create-topic")
    public ResponseEntity<?> createGroup(@RequestBody GroupCreateDto groupCreateDto) {

    }

    @PutMapping("/update-topic/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable String groupId) {

    }

    @DeleteMapping("/delete-topic/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {

    }
}
