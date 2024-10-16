package dev.ivanov.tasks_manager.user_service.configuration;

import dev.ivanov.tasks_manager.core.topics.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userCreatedEventsTopic() {
        return TopicBuilder.name(Topics.USER_CREATED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userDeletedEventsTopic() {
        return TopicBuilder.name(Topics.USER_DELETED_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userCreateEventsTopic() {
        return TopicBuilder.name(Topics.USER_CREATE_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userDeleteEventsTopic() {
        return TopicBuilder.name(Topics.USER_DELETE_EVENTS_TOPIC)
                .partitions(3)
                .build();
    }
}
