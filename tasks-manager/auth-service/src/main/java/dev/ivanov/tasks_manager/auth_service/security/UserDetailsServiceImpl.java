package dev.ivanov.tasks_manager.auth_service.security;

import dev.ivanov.tasks_manager.auth_service.exceptions.EntityNotFoundException;
import dev.ivanov.tasks_manager.auth_service.repositrories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("user with username " + username + " not found")));
    }
}
