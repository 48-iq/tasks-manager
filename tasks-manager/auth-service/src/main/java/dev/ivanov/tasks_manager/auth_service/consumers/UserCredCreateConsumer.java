package dev.ivanov.tasks_manager.auth_service.consumers;

import dev.ivanov.tasks_manager.auth_service.producers.UserCredCreatedProducer;
import dev.ivanov.tasks_manager.auth_service.services.UserCredService;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreateEvent;
import dev.ivanov.tasks_manager.auth_service.validators.UserCredCreateEventValidator;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
public class UserCredCreateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredCreateConsumer.class);

    @Autowired
    private UserCredService userCredService;

    @Autowired
    private UserCredCreateEventValidator validator;

    @Autowired
    private UserCredCreatedProducer userCredCreatedProducer;


    @KafkaListener(topics = Topics.USER_CRED_CREATE_EVENTS_TOPIC)
    public void handleUserCredCreateEvent(UserCredCreateEvent event) {
        try {
            var errors = new BeanPropertyBindingResult(event, "userCredCreateEvent");
            validator.validate(event, errors);
            if (errors.hasErrors()) {
                userCredCreatedProducer.sendError(event);
            } else {
                var user = userCredService.createUserCred(event);
                userCredCreatedProducer.sendSuccessful(user, event);
            }
        } catch (Exception e) {
            userCredCreatedProducer.sendError(event);
            throw e;
        }


    }

}
