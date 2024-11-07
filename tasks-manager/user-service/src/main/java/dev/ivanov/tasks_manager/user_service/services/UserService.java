package dev.ivanov.tasks_manager.user_service.services;

import dev.ivanov.tasks_manager.core.events.user.UserCreateEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeleteEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
import dev.ivanov.tasks_manager.user_service.dto.UserUpdateDto;
import dev.ivanov.tasks_manager.user_service.entities.postgres.User;
import dev.ivanov.tasks_manager.core.events.user.UserCreatedEvent;
import dev.ivanov.tasks_manager.user_service.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.user_service.producers.UserCreatedProducer;
import dev.ivanov.tasks_manager.user_service.producers.UserDeletedProducer;
import dev.ivanov.tasks_manager.user_service.repositories.postgres.UserRepository;
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

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private UserCreatedProducer userCreatedProducer;

    @Autowired
    private UserDeletedProducer userDeletedProducer;

    @Transactional
    public User createUser(String id) {
        var user = User.builder()
                .id(id)
                .name("")
                .surname("")
                .nickname("")
                .build();
        return userRepository.save(user);

    }

    public void createUser(UserCreateEvent userCreateEvent) {
        try {
            var user = createUser(userCreateEvent.getId());
            userCreatedProducer.sendSuccessful(userCreateEvent.getId(),
                    userCreateEvent.getTransactionId());
        } catch (Exception e) {
            userCreatedProducer.sendError(userCreateEvent.getId(), userCreateEvent.getTransactionId());
            throw e;
        }
    }



    @Transactional
    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + userId + "not found"));
        user.setName(userUpdateDto.getName());
        user.setSurname(userUpdateDto.getSurname());
        user.setEmail(userUpdateDto.getEmail());
        user.setNickname(userUpdateDto.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public void deleteUser(UserDeleteEvent userDeletedEvent) {
        try {
            deleteUser(userDeletedEvent.getId());
            userDeletedProducer.sendSuccessful(userDeletedEvent.getId(), userDeletedEvent.getTransactionId());
        } catch (Exception e) {
            userDeletedProducer.sendError(userDeletedEvent.getId(), userDeletedEvent.getTransactionId());
            throw e;
        }

    }

}
