package dev.ivanov.tasks_manager.user_service.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userCreatedEventsTopic() {
        return TopicBuilder.name("user-created-events-topic")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userDeletedEventsTopic() {
        return TopicBuilder.name("user-deleted-events-topic")
                .partitions(3)
                .build();
    }
}
