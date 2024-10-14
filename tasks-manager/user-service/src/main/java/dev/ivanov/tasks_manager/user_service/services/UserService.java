package dev.ivanov.tasks_manager.user_service.services;

import dev.ivanov.tasks_manager.core.events.user.UserCreateEvent;
import dev.ivanov.tasks_manager.user_service.dto.UserSignUpDto;
import dev.ivanov.tasks_manager.user_service.dto.UserUpdateDto;
import dev.ivanov.tasks_manager.user_service.entities.User;
import dev.ivanov.tasks_manager.core.events.user.UserCredCrateEvent;
import dev.ivanov.tasks_manager.user_service.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.user_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private KafkaTemplate<String, UserCredCrateEvent> kafkaTemplate;

    @Transactional
    public User createUser(UserSignUpDto signUpDto) {
        var uuidResponseEntity = restTemplate.getForEntity(gatewayHost + "/api/uuid", String.class);
        if (uuidResponseEntity.getStatusCode().isError())
            throw new InternalServerException("uuid service is not available");
        var uuid = uuidResponseEntity.getBody();
        var user = User.builder()
                .id(uuid)
                .username(signUpDto.getUsername())
                .name(signUpDto.getName())
                .surname(signUpDto.getSurname())
                .email(signUpDto.getEmail())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void createUser(UserCreateEvent userCreateEvent) {
        var user = User.builder()
                .id(userCreateEvent.getId())
                .username(userCreateEvent.getUsername())
                .email(userCreateEvent.getEmail())
                .name(userCreateEvent.getName())
                .surname(userCreateEvent.getSurname())
                .build();
        userRepository.save(user);
    }


    @Transactional
    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + userId + "not found"));
        user.setName(userUpdateDto.getName());
        user.setSurname(userUpdateDto.getSurname());
        user.setEmail(userUpdateDto.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
