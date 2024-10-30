package dev.ivanov.tasks_manager.auth_server.producers;

import dev.ivanov.tasks_manager.auth_server.services.AccountService;
import dev.ivanov.tasks_manager.core.events.auth.AccountDeletedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AccountDeletedEventsProducer {
    public static final Logger LOGGER = LoggerFactory.getLogger(AccountDeletedEventsProducer.class);

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private KafkaTemplate<String, AccountDeletedEvent> kafkaTemplate;

    public void send(String id) {
        var event = AccountDeletedEvent.builder()
                .id(id)
                .build();
        try {
            var result = kafkaTemplate.send(Topics.ACCOUNT_DELETED_EVENTS_TOPIC, event.getId(), event).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("message was not sent");
            accountService.rollbackDeletion(event.getId());
        }
    }
}
