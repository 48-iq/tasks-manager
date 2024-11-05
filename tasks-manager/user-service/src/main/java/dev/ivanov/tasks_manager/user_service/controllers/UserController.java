package dev.ivanov.tasks_manager.user_service.controllers;

import dev.ivanov.tasks_manager.user_service.dto.UserDto;
import dev.ivanov.tasks_manager.user_service.dto.UserUpdateDto;
import dev.ivanov.tasks_manager.user_service.producers.UserDeletedProducer;
import dev.ivanov.tasks_manager.user_service.repositories.postgres.UserRepository;
import dev.ivanov.tasks_manager.user_service.services.UserService;
import dev.ivanov.tasks_manager.user_service.validators.UserUpdateDtoValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDeletedProducer userDeletedProducer;

    @Autowired
    private UserUpdateDtoValidator userUpdateDtoValidator;


    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable String userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(UserDto.from(userOptional.get()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable String userId,
                                    @RequestBody UserUpdateDto userUpdateDto) {
        try {
            var updatedUser = userService.updateUser(userId, userUpdateDto);
            return ResponseEntity.ok(UserDto.from(updatedUser));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
