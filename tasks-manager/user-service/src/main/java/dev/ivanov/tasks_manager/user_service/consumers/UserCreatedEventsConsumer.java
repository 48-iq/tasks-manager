package dev.ivanov.tasks_manager.user_service.consumers;

import dev.ivanov.tasks_manager.core.events.UserDeleteEvent;
import dev.ivanov.tasks_manager.user_service.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "user-created-events-topic")
public class UserCreatedEventsConsumer {

    @Autowired
    private UserService userService;

    @KafkaHandler
    public void handle(UserDeleteEvent event) {
        userService.deleteUser(event.getId());
    }
}
