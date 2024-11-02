package dev.ivanov.tasks_manager.core.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface Authorizer {
    boolean authorize(Authentication authentication, HttpServletRequest request, Map<String, String> variables);
}
