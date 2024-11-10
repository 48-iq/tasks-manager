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
        userRepository.save(new User(id));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
