package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.dto.topic.TopicCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.topic.TopicDto;
import dev.ivanov.tasks_manager.group_service.dto.topic.TopicUpdateDto;
import dev.ivanov.tasks_manager.group_service.entities.postgres.Topic;
import dev.ivanov.tasks_manager.group_service.exceptions.InternalServerException;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.GroupRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.TopicRepository;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Value("${app.gateway.host}")
    private String gatewayHost;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public List<TopicDto> findTopicsByGroup(String groupId) {
        var topics = topicRepository.findTopicsByGroup(groupId);
        return topics.stream().map(TopicDto::from).toList();
    }

    @Transactional
    public TopicDto createTopic(String userId, String groupId, TopicCreateDto topicCreateDto) {
        var uuidResponseEntity = restTemplate.getForEntity(gatewayHost + "/api/uuid", String.class);
        if (uuidResponseEntity.getStatusCode().isError())
            throw new InternalServerException("uuid service is not available");

        var topicId = uuidResponseEntity.getBody();
        var topic = Topic.builder()
                .id(topicId)
                .createdAt(LocalDateTime.now())
                .creator(userRepository.getReferenceById(userId))
                .group(groupRepository.getReferenceById(groupId))
                .description(topicCreateDto.getDescription())
                .title(topicCreateDto.getTitle())
                .theme(topicCreateDto.getTheme())
                .complexity(topicCreateDto.getComplexity())
                .importance(topicCreateDto.getImportance())
                .build();

        var savedTopic = topicRepository.save(topic);
        return TopicDto.from(topic);
    }

    @Transactional
    public TopicDto updateTopic(String topicId, TopicUpdateDto topicUpdateDto) {

    }
}
