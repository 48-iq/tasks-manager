package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.user.UserCreateEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredDeleteEvent;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredDeletedEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
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
public class UserDeleteTransaction {
    private final Logger LOGGER = LoggerFactory.getLogger(UserCreateTransaction.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private UuidService uuidService;

    @Autowired
    private TransactionPartRepository transactionPartRepository;

    private static final String USER_DELETED_PART = "USER_DELETED_PART";



    @KafkaListener(topics = Topics.USER_DELETED_EVENTS_TOPIC)
    public void startTransaction(UserDeletedEvent userDeletedEvent) {
        var transactionId = uuidService.generate();
        var id = uuidService.getFullId(transactionId, USER_DELETED_PART);
        var transactionPart = TransactionPart.builder()
                .id(id)
                .event(userDeletedEvent)
                .build();
        transactionPartRepository.save(transactionPart);

        var userCredDeleteEvent = UserCredDeleteEvent.builder()
                .transactionId(transactionId)
                .id(userDeletedEvent.getId())
                .build();
        try {
            var result = kafkaTemplate.send(Topics.USER_CRED_DELETE_EVENTS_TOPIC,
                    userCredDeleteEvent.getId(),
                    userCredDeleteEvent).get();
        } catch (InterruptedException|ExecutionException e) {
            rollbackDeletedUser(transactionId);
            LOGGER.error(e.getMessage());
        }
    }

    @KafkaListener(topics = Topics.USER_CRED_DELETED_EVENTS_TOPIC)
    public void userCredDeleted(UserCredDeletedEvent userCredDeletedEvent) {
        if (userCredDeletedEvent.isError()) {
            rollbackDeletedUser(userCredDeletedEvent.getTransactionId());
        } else {
            commitTransaction(userCredDeletedEvent.getTransactionId());
        }
    }

    public void rollbackDeletedUser(String transactionId) {
        var id = uuidService.getFullId(transactionId, USER_DELETED_PART);

        var transactionPart = transactionPartRepository.findById(id)
                .orElseThrow(() -> new TransactionPartNotFoundException(id));

        var userDeletedEvent = (UserDeletedEvent) transactionPart.getEvent();

        var userCreateEvent = UserCreateEvent.builder()
                .transactionId(transactionId)
                .id(userDeletedEvent.getId())
                .name(userDeletedEvent.getName())
                .surname(userDeletedEvent.getSurname())
                .username(userDeletedEvent.getUsername())
                .email(userDeletedEvent.getEmail())
                .build();

        transactionPartRepository.deleteById(id);
        try {
            var result = kafkaTemplate.send(Topics.USER_CREATE_EVENTS_TOPIC,
                    userCreateEvent.getId(),
                    userCreateEvent).get();
        } catch (ExecutionException|InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void commitTransaction(String transactionId) {
        var userDeletedPartId = uuidService.getFullId(transactionId, USER_DELETED_PART);
        transactionPartRepository.deleteById(userDeletedPartId);
    }
}
