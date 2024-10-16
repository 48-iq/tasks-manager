package dev.ivanov.tasks_manager.user_service.producers;

import dev.ivanov.tasks_manager.core.events.user.UserCreatedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.user_service.dto.UserSignUpDto;
import dev.ivanov.tasks_manager.user_service.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCreatedProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreatedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void send(UserSignUpDto userSignUpDto, User user) {
        var userCreatedEvent = UserCreatedEvent.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(userSignUpDto.getUsername())
                .password(userSignUpDto.getPassword())
                .role(userSignUpDto.getRole())
                .adminPassword(userSignUpDto.getAdminPassword())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CREATED_EVENTS_TOPIC,
                    userCreatedEvent.getId(),
                    userCreatedEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
