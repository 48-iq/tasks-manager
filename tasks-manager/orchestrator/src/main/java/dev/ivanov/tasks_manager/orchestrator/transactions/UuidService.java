package dev.ivanov.tasks_manager.orchestrator.transactions;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {
    public String generate() {
        return UUID.randomUUID().toString();
    }

    public String getFullId(String transactionId, String partId) {
        return transactionId + ":" + partId;
    }
}
