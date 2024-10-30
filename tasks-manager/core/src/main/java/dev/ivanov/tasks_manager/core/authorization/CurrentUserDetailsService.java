package dev.ivanov.tasks_manager.core.authorization;

import org.springframework.security.core.userdetails.UserDetails;

public interface CurrentUserDetailsService {
    UserDetails getCurrentUserDetails();
}
