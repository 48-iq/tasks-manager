package dev.ivanov.tasks_manager.user_service.consumers;

import dev.ivanov.tasks_manager.core.events.UserCreateEvent;
import dev.ivanov.tasks_manager.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "user-deleted-events-topic")
public class UserDeletedEventsConsumer {
    @Autowired
    private UserService userService;

    @KafkaHandler
    public void handle(UserCreateEvent userCreateEvent) {
        userService.createUser(userCreateEvent);
    }
}
