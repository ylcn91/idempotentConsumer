package com.doksanbir.idempotentconsumer.consumer;

import com.doksanbir.idempotentconsumer.model.TransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConverter {

    private final ObjectMapper objectMapper;

    public TransactionEventConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TransactionEvent convertToTransactionEvent(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, TransactionEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to TransactionEvent", e);
        }
    }
}

