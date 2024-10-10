package dev.ivanov.tasks_manager.user_service.producers;

public interface EventProducer<T> {
    void send(T t);
}
