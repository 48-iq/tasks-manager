package dev.ivanov.tasks_manager.user_service.producers;

import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.user_service.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserDeletedProducer {
    private Logger LOGGER = LoggerFactory.getLogger(UserDeletedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    public void send(User user) {
        var userDeletedEvent = UserDeletedEvent.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_DELETED_EVENTS_TOPIC,
                    userDeletedEvent.getId(),
                    userDeletedEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
