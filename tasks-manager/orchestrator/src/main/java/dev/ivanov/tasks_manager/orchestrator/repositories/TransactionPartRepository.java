package dev.ivanov.tasks_manager.orchestrator.repositories;

import dev.ivanov.tasks_manager.orchestrator.entities.TransactionPart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPartRepository extends CrudRepository<TransactionPart, String> {
}
