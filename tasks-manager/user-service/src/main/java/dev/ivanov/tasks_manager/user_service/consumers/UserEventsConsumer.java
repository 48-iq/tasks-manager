package dev.ivanov.tasks_manager.user_service.consumers;

import dev.ivanov.tasks_manager.core.events.user.UserCreateEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeleteEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.user_service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventsConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventsConsumer.class);

    @Autowired
    private UserService userService;

    @KafkaListener(topics = Topics.USER_CREATE_EVENTS_TOPIC)
    public void handleUserCreateEvent(UserCreateEvent userCreateEvent) {
        userService.createUser(userCreateEvent);
    }

    @KafkaListener(topics = Topics.USER_DELETE_EVENTS_TOPIC)
    public void handleUserDeleteEvents(UserDeleteEvent event) {
        userService.deleteUser(event);
    }
}
