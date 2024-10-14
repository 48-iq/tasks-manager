package dev.ivanov.tasks_manager.auth_service.services;

import dev.ivanov.tasks_manager.auth_service.entities.Role;
import dev.ivanov.tasks_manager.auth_service.entities.UserCred;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import dev.ivanov.tasks_manager.core.events.usercred.UserCredCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCredService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserCred createUserCred(UserCredCreateEvent event) {
        var user = UserCred.builder()
                .id(event.getId())
                .username(event.getUsername())
                .role(Role.valueOf(event.getRole()))
                .password(passwordEncoder.encode(event.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public void deleteUserCred(String id) {
        userRepository.deleteById(id);
    }
}
