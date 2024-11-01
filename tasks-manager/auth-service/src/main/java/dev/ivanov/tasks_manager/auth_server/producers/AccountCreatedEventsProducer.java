package dev.ivanov.tasks_manager.auth_server.producers;

import dev.ivanov.tasks_manager.auth_server.entities.postgres.Account;
import dev.ivanov.tasks_manager.auth_server.services.AccountService;
import dev.ivanov.tasks_manager.core.events.auth.AccountCreatedEvent;
import dev.ivanov.tasks_manager.core.topics.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AccountCreatedEventsProducer {
    public static final Logger LOGGER = LoggerFactory.getLogger(AccountCreatedEventsProducer.class);

    @Autowired
    private KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void send(String id) {
        var event = AccountCreatedEvent.builder()
                .id(id)
                .build();
        try {
            var result = kafkaTemplate.send(Topics.ACCOUNT_CREATED_EVENTS_TOPIC, event.getId(), event).get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("message was not sent");
            accountService.rollbackCreation(event.getId());
        }

    }
}
