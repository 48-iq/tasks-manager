package dev.ivanov.tasks_manager.group_service.services;

import dev.ivanov.tasks_manager.group_service.entities.postgres.User;
import dev.ivanov.tasks_manager.group_service.repositories.postgres.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(String id) {
        if (!userRepository.existsById(id)) {
            var user = new User(id);
            userRepository.save(user);
        }
    }

    public void deleteUser(String id) {

    }
}
