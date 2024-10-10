package dev.ivanov.tasks_manager.auth_service.listeners;

import dev.ivanov.tasks_manager.core.events.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.auth_service.services.AuthService;
import dev.ivanov.tasks_manager.auth_service.validators.UserCredCreateEventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
public class UserCredCreateListener {
    @Autowired
    private AuthService authService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private UserCredCreateEventValidator validator;

    @KafkaListener(topics = "user-cred-create-events-topic")
    public void handleUserCredCreateEvent(UserCredCreateEvent event) {
        var errors = new BeanPropertyBindingResult(event, "userCredCreateEvent");
        validator.validate(event, errors);
        if (errors.hasErrors()) {
            var userCredCreatedEvent = UserCredCreatedEvent.builder()
                    .id(event.getId())
                    .isError(true)
                    .build();
            kafkaTemplate.send("user-cred-created-events-topic",
                    userCredCreatedEvent.getId(), userCredCreatedEvent);
        } else {
            authService.signUp(event);
        }
    }
}
