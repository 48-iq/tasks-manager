package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.dto.TopicCreateDto;
import dev.ivanov.tasks_manager.group_service.dto.TopicDto;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.TopicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;


    @Transactional
    public List<TopicDto> findTopicsByGroup(String groupId) {
        var topics = topicRepository.findTopicsByGroup(groupId);
        return topics.stream().map(TopicDto::from).toList();
    }

    @Transactional
    public TopicDto createTopic(String userId, String groupId, TopicCreateDto topicCreateDto) {

    }
}
