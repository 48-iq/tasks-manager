package dev.ivanov.tasks_manager.user_service.consumers;

import dev.ivanov.tasks_manager.core.events.auth.TokenAddedToBlacklistEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.user_service.entities.redis.Token;
import dev.ivanov.tasks_manager.user_service.repositories.redis.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TokenAddedToBlacklistEventsConsumer {

    @Value("${app.jwt.expiration-access}")
    private Long expiration;

    @Autowired
    private TokenRepository tokenRepository;

    @KafkaListener(topics = Topics.TOKEN_ADDED_TO_BLACKLIST_EVENTS_TOPIC)
    public void handleTokenAddedToBlacklistEvent(TokenAddedToBlacklistEvent event) {
        var token = Token.builder()
                .id(event.getToken())
                .expiration(expiration)
                .token(event.getToken())
                .build();

    }

}
