package dev.ivanov.tasks_manager.user_service.producers;

import dev.ivanov.tasks_manager.core.events.user.UserCredCrateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCreatedProducer {

    private Logger LOGGER = LoggerFactory.getLogger(UserCreatedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserCredCrateEvent> kafkaTemplate;

    public void send(UserCredCrateEvent userCreatedEvent) {
        try {
            var result = kafkaTemplate.send("user-created-events-topic", userCreatedEvent.getId(), userCreatedEvent)
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("user-created-events-topic message not sent");
        }
    }

}
