package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.auth.AccountCreatedEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountCreationCommitEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountCreationRollbackEvent;
import dev.ivanov.tasks_manager.core.events.user.UserCreateEvent;
import dev.ivanov.tasks_manager.core.events.user.UserCreatedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
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
    private IdService idService;

    private static final String USER_CREATED_PART = "USER_CREATED_PART";


    //start transaction
    //send user create event to user service
    @KafkaListener(topics = Topics.ACCOUNT_CREATED_EVENTS_TOPIC)
    public void startTransaction(AccountCreatedEvent event) {
        try {
            var userCreateEvent = UserCreateEvent.builder()
                    .id(event.getId())
                    .transactionId(idService.generate())
                    .build();
            var result = kafkaTemplate.send(Topics.USER_CREATE_EVENTS_TOPIC,
                    userCreateEvent.getId(), userCreateEvent).get();
        } catch (InterruptedException| ExecutionException e) {
            logSendError(e);
        }
    }

    @KafkaListener(topics = Topics.USER_CREATED_EVENTS_TOPIC)
    public void handleUserCreationResult(UserCreatedEvent event) {
        try {
            //handle successful user creation
            if (!event.isError()) {
                var accountCreationCommitEvent = AccountCreationCommitEvent.builder()
                        .id(event.getId())
                        .transactionId(event.getTransactionId())
                        .build();   
                var result = kafkaTemplate.send(Topics.ACCOUNT_CREATION_COMMIT_EVENTS_TOPIC,
                        accountCreationCommitEvent.getId(), accountCreationCommitEvent).get();
            }
            //handle failure user creation
            else {
                var accountCreationRollbackEvent = AccountCreationRollbackEvent.builder()
                        .id(event.getId())
                        .transactionId(event.getTransactionId())
                        .build();
                var result = kafkaTemplate.send(Topics.ACCOUNT_CREATION_ROLLBACK_EVENTS_TOPIC,
                        accountCreationRollbackEvent.getId(), accountCreationRollbackEvent);
            }
        } catch (InterruptedException|ExecutionException e) {
            logSendError(e);
        }
    }

    private void logSendError(Exception e) {
        LOGGER.error("message was no sent: {}", e.getMessage());
    }

}
