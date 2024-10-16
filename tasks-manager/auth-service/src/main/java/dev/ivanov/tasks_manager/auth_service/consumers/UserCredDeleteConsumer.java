package dev.ivanov.tasks_manager.auth_service.consumers;

import dev.ivanov.tasks_manager.auth_service.producers.UserCredDeletedProducer;
import dev.ivanov.tasks_manager.auth_service.services.UserCredService;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredDeleteEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCredDeleteConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCredDeleteConsumer.class);

    @Autowired
    private UserCredService userCredService;

    @Autowired
    private UserCredDeletedProducer userCredDeletedProducer;


    @KafkaListener(topics = Topics.USER_CRED_DELETE_EVENTS_TOPIC)
    public void handleUserCredCreateEvent(UserCredDeleteEvent event) {
        try {
            userCredService.deleteUserCred(event.getId());
            userCredDeletedProducer.sendSuccessful(event);

        } catch (Exception e) {
            userCredDeletedProducer.sendError(event);
            throw e;
        }


    }


}
