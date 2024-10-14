package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.user.UserCredCrateEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreateEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreatedEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeleteEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import dev.ivanov.tasks_manager.orchestrator.entities.TransactionPart;
import dev.ivanov.tasks_manager.orchestrator.event.TransactionPartNotFoundException;
import dev.ivanov.tasks_manager.orchestrator.repositories.TransactionPartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class UserCreateTransaction {
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreateTransaction.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TransactionPartRepository transactionPartRepository;

    @Autowired
    private UuidService uuidService;

    private static final String USER_CREATED_PART = "USER_CREATED_PART";


    @KafkaListener(topics = Topics.USER_CREATED_EVENTS_TOPIC)
    public void startTransaction(UserCredCrateEvent event) {
        var transactionId = uuidService.generate();
        var id = uuidService.getFullId(transactionId, USER_CREATED_PART);

        var transactionPart = TransactionPart.builder()
                .id(id)
                .event(event)
                .build();
        transactionPartRepository.save(transactionPart);

        var userCredCreateEvent = UserCredCreateEvent.builder()
                .transactionId(transactionId)
                .id(event.getId())
                .username(event.getUsername())
                .password(event.getPassword())
                .role(event.getRole())
                .adminPassword(event.getAdminPassword())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_UPDATE_EVENTS_TOPIC,
                    event.getId(), userCredCreateEvent).get();
        } catch (ExecutionException | InterruptedException e) {
            rollbackCreatedUser(id);
            LOGGER.error(e.getMessage());
        }
    }

    @KafkaListener(topics = Topics.USER_CRED_CREATED_EVENTS_TOPIC)
    public void userCredCreated(UserCredCreatedEvent event) {
        var transactionId = event.getTransactionId();
        if (event.isError()) {
            rollbackCreatedUser(transactionId);
        } else {
            commitTransaction(transactionId);
        }
    }

    public void rollbackCreatedUser(String transactionId) {
        var id = uuidService.getFullId(transactionId, USER_CREATED_PART);

        var transactionPart = transactionPartRepository.findById(id)
                .orElseThrow(() -> new TransactionPartNotFoundException(id));

        var userCreatedEvent = (UserCredCrateEvent) transactionPart.getEvent();

        var userDeleteEvent = UserDeleteEvent.builder()
                .transactionId(transactionId)
                .id(userCreatedEvent.getId())
                .build();

        transactionPartRepository.deleteById(id);

        try {
            var result = kafkaTemplate.send(Topics.USER_CREATED_EVENTS_TOPIC,
                    userDeleteEvent.getId(), userDeleteEvent).get();

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void commitTransaction(String transactionId) {
        var userCreatedPartId = uuidService.getFullId(transactionId, USER_CREATED_PART);
        transactionPartRepository.deleteById(userCreatedPartId);
    }

}
