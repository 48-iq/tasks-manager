package dev.ivanov.tasks_manager.auth_server.producers;

import dev.ivanov.tasks_manager.auth_server.security.JwtUtils;
import dev.ivanov.tasks_manager.core.events.auth.TokenAddedToBlacklistEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class TokenAddedToBlacklistProducer {
    public static final Logger LOGGER = LoggerFactory.getLogger(TokenAddedToBlacklistProducer.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private KafkaTemplate<String, TokenAddedToBlacklistEvent> kafkaTemplate;

    public void send(String id, String token) {
        var event = TokenAddedToBlacklistEvent.builder()
                .id(id)
                .token(token)
                .build();

        try {
            var result = kafkaTemplate.send(Topics.TOKEN_ADDED_TO_BLACKLIST_EVENTS_TOPIC, event.getId(), event).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("message was not sent");
        }
    }
}
