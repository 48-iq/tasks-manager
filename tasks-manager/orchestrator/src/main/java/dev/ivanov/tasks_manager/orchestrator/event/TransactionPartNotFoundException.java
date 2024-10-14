package dev.ivanov.tasks_manager.orchestrator.event;

import lombok.experimental.StandardException;

@StandardException
public class TransactionPartNotFoundException extends RuntimeException {
    public TransactionPartNotFoundException(String id) {
        super(id + " not found");
    }
}
