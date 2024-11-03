package dev.ivanov.tasks_manager.auth_server.consumers;

import dev.ivanov.tasks_manager.auth_server.services.AccountService;
import dev.ivanov.tasks_manager.core.events.auth.AccountCreationCommitEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountCreationRollbackEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountDeletionCommitEvent;
import dev.ivanov.tasks_manager.core.events.auth.AccountDeletionRollbackEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventsConsumer {

    @Autowired
    private AccountService accountService;

    @KafkaListener(topics = Topics.ACCOUNT_CREATION_COMMIT_EVENTS_TOPIC)
    public void handleAccountCreationCommitEvent(AccountCreationCommitEvent event) {
        log.info("event was handled {}", event);
        accountService.commitCreation(event.getId());
    }

    @KafkaListener(topics = Topics.ACCOUNT_CREATION_ROLLBACK_EVENTS_TOPIC)
    public void handleAccountCreationRollbackEvent(AccountCreationRollbackEvent event) {
        accountService.rollbackCreation(event.getId());
    }

    @KafkaListener(topics = Topics.ACCOUNT_DELETION_COMMIT_EVENTS_TOPIC)
    public void handleAccountDeletionCommitEvent(AccountDeletionCommitEvent event) {
        accountService.commitDeletion(event.getId());
    }

    @KafkaListener(topics = Topics.ACCOUNT_DELETION_ROLLBACK_EVENTS_TOPIC)
    public void handleAccountDeletionRollbackEvent(AccountDeletionRollbackEvent event) {
        accountService.rollbackDeletion(event.getId());
    }
}
