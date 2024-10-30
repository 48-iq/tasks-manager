package dev.ivanov.tasks_manager.core.security;

public interface BlackListJwtCheckService {
    boolean isOnBlacklist(String jwt);
}
