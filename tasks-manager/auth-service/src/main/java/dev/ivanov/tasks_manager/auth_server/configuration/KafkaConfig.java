package dev.ivanov.tasks_manager.auth_server.configuration;

import dev.ivanov.tasks_manager.core.topics.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic accountCreatedEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_CREATED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountDeletedEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_DELETED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic tokenAddedToBlacklistEventsTopic() {
        return TopicBuilder.name(Topics.TOKEN_ADDED_TO_BLACKLIST_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountCreationCommitEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_CREATION_COMMIT_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountCreationRollbackEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_CREATION_ROLLBACK_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountDeletionCommitEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_DELETION_COMMIT_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic accountDeletionRollbackEventsTopic() {
        return TopicBuilder.name(Topics.ACCOUNT_DELETION_ROLLBACK_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

}
