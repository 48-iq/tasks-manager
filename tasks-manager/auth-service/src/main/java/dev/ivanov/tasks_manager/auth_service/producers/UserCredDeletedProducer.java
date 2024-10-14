package dev.ivanov.tasks_manager.auth_service.producers;

import dev.ivanov.tasks_manager.auth_service.entities.UserCred;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredDeleteEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredDeletedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCredDeletedProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredDeletedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserCredDeletedEvent> kafkaTemplate;

    public void sendSuccessful(UserCredDeleteEvent event) {

        var userCredDeletedEvent = UserCredDeletedEvent.builder()
                .transactionId(event.getTransactionId())
                .isError(false)
                .id(event.getId())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_DELETED_EVENTS_TOPIC,
                    userCredDeletedEvent.getId(), userCredDeletedEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void sendError(UserCredDeleteEvent event) {
        var userCredDeletedEvent = UserCredDeletedEvent.builder()
                .transactionId(event.getTransactionId())
                .id(event.getId())
                .isError(true)
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_DELETED_EVENTS_TOPIC,
                    userCredDeletedEvent.getId(),
                    userCredDeletedEvent).get();
        } catch (ExecutionException|InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
