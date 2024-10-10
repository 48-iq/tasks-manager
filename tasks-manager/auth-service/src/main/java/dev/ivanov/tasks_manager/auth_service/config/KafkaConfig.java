package dev.ivanov.tasks_manager.auth_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userCredCreatedEventsTopic() {
        return TopicBuilder.name("user-cred-created-events-topic")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic userCredCreateEventsTopic() {
        return TopicBuilder.name("user-cred-create-events-topic")
                .partitions(3)
                .build();
    }



}
