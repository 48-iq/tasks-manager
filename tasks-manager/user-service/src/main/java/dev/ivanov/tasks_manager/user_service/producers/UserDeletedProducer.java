package dev.ivanov.tasks_manager.user_service.producers;

import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.user_service.entities.postgres.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserDeletedProducer {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserDeletedProducer.class);

    @Autowired
    private KafkaTemplate<String, UserDeletedEvent> kafkaTemplate;

    public void sendSuccessful(String id, String transactionId) {
        send(id, transactionId, false);
    }

    public void sendError(String id, String transactionId) {
        send(id, transactionId, true);
    }

    private void send(String id, String transactionId, boolean isError) {
        var userDeletedEvent = UserDeletedEvent.builder()
                .id(id)
                .transactionId(transactionId)
                .isError(isError)
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
