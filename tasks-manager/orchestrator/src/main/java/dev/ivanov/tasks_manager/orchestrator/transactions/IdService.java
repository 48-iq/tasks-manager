package dev.ivanov.tasks_manager.orchestrator.transactions;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class IdService {
    private long key = ZonedDateTime.now().toInstant().getEpochSecond();
    private String random = String.valueOf(new Random(key).nextDouble());

    public String generate() {
        return UUID.randomUUID().toString() + "_" + random;
    }

    public String getFullId(String transactionId, String partId) {
        return transactionId + ":" + partId;
    }
}
