package dev.ivanov.tasks_manager.orchestrator.transactions;

import dev.ivanov.tasks_manager.core.events.auth.AccountDeletedEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountDeletionCommitEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountDeletionRollbackEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeleteEvent;
import dev.ivanov.tasks_manager.core.events.user.UserDeletedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.apache.kafka.common.internals.Topic;
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
    private IdService idService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public static final String USER_DELETED_PART = "user deleted part";

    //start transaction
    //send user delete event to user service
    @KafkaListener(topics = Topics.ACCOUNT_DELETED_EVENTS_TOPIC)
    public void startTransaction(AccountDeletedEvent event) {
        try {
            var userDeleteEvent = UserDeleteEvent.builder()
                    .id(event.getId())
                    .transactionId(idService.generate())
                    .build();
            var result = kafkaTemplate.send(Topics.USER_DELETE_EVENTS_TOPIC, userDeleteEvent.getId(), userDeleteEvent).get();
        } catch (InterruptedException|ExecutionException e) {
            logSendError(e);
        }
    }

    @KafkaListener(topics = Topics.USER_DELETED_EVENTS_TOPIC)
    public void handleUserDeletionResult(UserDeletedEvent event) {
        try {
            //handle successful user deletion
            if (!event.isError()) {
                var accountDeletionCommitEvent = AccountDeletionCommitEvent.builder()
                        .transactionId(event.getTransactionId())
                        .id(event.getId())
                        .build();
                var result = kafkaTemplate.send(Topics.ACCOUNT_DELETION_COMMIT_EVENTS_TOPIC,
                        accountDeletionCommitEvent.getId(), accountDeletionCommitEvent).get();
            }
            //handle failure user creation
            else {
                var accountDeletionRollbackEvent = AccountDeletionRollbackEvent.builder()
                        .id(event.getId())
                        .transactionId(event.getTransactionId())
                        .build();
                var result = kafkaTemplate.send(Topics.ACCOUNT_DELETION_ROLLBACK_EVENTS_TOPIC,
                        accountDeletionRollbackEvent.getId(), accountDeletionRollbackEvent);
            }
        } catch (ExecutionException|InterruptedException e) {
            logSendError(e);
        }
    }

    private void logSendError(Exception e) {
        LOGGER.error("message was no sent: {}", e.getMessage());
    }
}
