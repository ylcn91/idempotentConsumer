package com.doksanbir.idempotentconsumer.consumer;

import com.doksanbir.idempotentconsumer.model.TransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventConverter {

    private final ObjectMapper objectMapper;
    
    public TransactionEvent convertToTransactionEvent(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, TransactionEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to TransactionEvent", e);
        }
    }
}

