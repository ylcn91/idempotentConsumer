package com.doksanbir.idempotentconsumer.repository;

import com.doksanbir.idempotentconsumer.model.ProcessedMessage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ProcessedMessageRepository extends ReactiveCrudRepository<ProcessedMessage, String> {

    @Query("INSERT INTO processed_messages (message_id, processed_at) VALUES (:messageId, :processedAt)")
    Mono<ProcessedMessage> insertMessage(String messageId, LocalDateTime processedAt);
}


