package dev.ivanov.tasks_manager.auth_service.producers;

import dev.ivanov.tasks_manager.auth_service.entities.UserCred;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCredCreatedProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredCreatedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserCredCreatedEvent> kafkaTemplate;

    public void sendSuccessful(UserCred user, UserCredCreateEvent event) {

        var userCredCreatedEvent = UserCredCreatedEvent.builder()
                .transactionId(event.getTransactionId())
                .isError(false)
                .id(user.getId())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_CREATED_EVENTS_TOPIC,
                    userCredCreatedEvent.getId(), userCredCreatedEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void sendError(UserCredCreateEvent event) {
        var userCredCreatedEvent = UserCredCreatedEvent.builder()
                .transactionId(event.getTransactionId())
                .id(event.getId())
                .isError(true)
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_CREATED_EVENTS_TOPIC,
                    userCredCreatedEvent.getId(),
                    userCredCreatedEvent).get();
        } catch (ExecutionException|InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
