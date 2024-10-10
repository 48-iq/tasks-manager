package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.UserCreatedEvent;
import dev.ivanov.tasks_manager.core.events.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.core.events.UserDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCreateTransaction {
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreateTransaction.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "user-created-events-topic")
    public void startTransaction(UserCreatedEvent event) {
        var userCredCreateEvent = UserCredCreateEvent.builder()
                .id(event.getId())
                .username(event.getUsername())
                .password(event.getPassword())
                .role(event.getRole())
                .adminPassword(event.getAdminPassword())
                .build();
        try {
            var result = kafkaTemplate.send("user-cred-create-events-topic",
                    event.getId(), userCredCreateEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @KafkaListener(topics = "user-cred-created-events-topic")
    public void action1(UserCredCreatedEvent event) {
        if (event.isError()) {
            rollback1(event);
        }
    }

    public void rollback1(UserCredCreatedEvent event) {
        var userDeleteEvent = UserDeleteEvent.builder()
                .id(event.getId())
                .build();
        try {
            var result = kafkaTemplate.send("user-created-events-topic",
                    event.getId(), userDeleteEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
