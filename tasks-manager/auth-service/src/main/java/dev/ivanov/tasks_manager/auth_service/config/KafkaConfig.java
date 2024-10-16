package dev.ivanov.tasks_manager.auth_service.config;

import dev.ivanov.tasks_manager.core.topics.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic userCredCreatedEventsTopic() {
        return TopicBuilder.name(Topics.USER_CRED_CREATED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userCredDeletedEventsTopic() {
        return TopicBuilder.name(Topics.USER_CRED_DELETED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userCredCreateEventsTopic() {
        return TopicBuilder.name(Topics.USER_CRED_CREATE_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userCredDeleteEventsTopic() {
        return TopicBuilder.name(Topics.USER_CRED_DELETE_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }
}
