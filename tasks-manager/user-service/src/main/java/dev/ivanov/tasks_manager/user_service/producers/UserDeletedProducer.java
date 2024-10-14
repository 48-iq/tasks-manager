package dev.ivanov.tasks_manager.user_service.producers;

import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
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

    public void send(UserDeletedEvent userDeletedEvent) {
        try {
            var result = kafkaTemplate.send("user-created-events-topic",
                            userDeletedEvent.getId(), userDeletedEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("user-deleted-events-topic message not sent");
        }
    }
}
