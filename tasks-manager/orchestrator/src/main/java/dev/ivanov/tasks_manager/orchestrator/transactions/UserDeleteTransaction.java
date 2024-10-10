package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.UserCreateEvent;
import dev.ivanov.tasks_manager.core.events.UserCredDeleteEvent;
import dev.ivanov.tasks_manager.core.events.UserCredDeletedEvent;
import dev.ivanov.tasks_manager.core.events.UserDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserDeleteTransaction {
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreateTransaction.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-deleted-events-topic")
    public void startTransaction(UserDeletedEvent userDeletedEvent) {
        var userCredDeleteEvent = UserCredDeleteEvent.builder()
                .id(userDeletedEvent.getId())
                .name(userDeletedEvent.getName())
                .surname(userDeletedEvent.getSurname())
                .email(userDeletedEvent.getEmail())
                .username(userDeletedEvent.getUsername())
                .build();
        try {
            var result = kafkaTemplate.send("user-cred-delete-events-topic", userCredDeleteEvent.getId(),
                    userCredDeleteEvent).get();
        } catch (InterruptedException|ExecutionException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @KafkaListener(topics = "user-cred-delete-events-topic")
    public void action1(UserCredDeletedEvent userCredDeletedEvent) {
        var userCreateEvent = UserCreateEvent.builder()
                .id(userCredDeletedEvent.getId())
                .name(userCredDeletedEvent.getName())
                .surname(userCredDeletedEvent.getSurname())
                .email(userCredDeletedEvent.getEmail())
                .username(userCredDeletedEvent.getUsername())
                .build();
        if (userCredDeletedEvent.isError()) {
            try {
                var result = kafkaTemplate.send("user-deleted-events-topic", userCreateEvent.getId(),
                        userCreateEvent).get();
            } catch (ExecutionException|InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
