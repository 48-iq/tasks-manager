package dev.ivanov.tasks_manager.user_service.controllers;

import dev.ivanov.tasks_manager.core.events.UserCreatedEvent;
import dev.ivanov.tasks_manager.core.events.UserDeletedEvent;
import dev.ivanov.tasks_manager.user_service.dto.UserDto;
import dev.ivanov.tasks_manager.user_service.dto.UserSignUpDto;
import dev.ivanov.tasks_manager.user_service.dto.UserUpdateDto;
import dev.ivanov.tasks_manager.user_service.producers.EventProducer;
import dev.ivanov.tasks_manager.user_service.repositories.UserRepository;
import dev.ivanov.tasks_manager.user_service.services.UserService;
import dev.ivanov.tasks_manager.user_service.validators.UserSignUpRequestDtoValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
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
    private UserSignUpRequestDtoValidator userSignUpRequestDtoValidator;

    @Autowired
    private EventProducer<UserCreatedEvent> userCreatedProducer;

    @Autowired
    private EventProducer<UserDeletedEvent> userDeletedProducer;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDto requestDto) {
        var errors = new BeanPropertyBindingResult(requestDto, "userSignUpRequestDto");
        userSignUpRequestDtoValidator.validate(requestDto, errors);

        if (errors.hasErrors())
            return ResponseEntity.badRequest().body(
                    errors.getAllErrors().stream().map(e -> e.getDefaultMessage()).toList()
            );
        var savedUser = userService.createUser(requestDto);
        var userCreatedEvent = UserCreatedEvent.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .password(requestDto.getPassword())
                .name(savedUser.getName())
                .surname(savedUser.getSurname())
                .email(savedUser.getEmail())
                .role(requestDto.getRole())
                .adminPassword(requestDto.getAdminPassword())
                .build();
        userCreatedProducer.send(userCreatedEvent);
        return ResponseEntity.ok().build();
    }

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

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            userService.deleteUser(userId);
            var userDeletedEvent = UserDeletedEvent.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .build();
            userDeletedProducer.send(userDeletedEvent);
        }
        return ResponseEntity.ok().build();
    }
}
