package com.doksanbir.idempotentconsumer.model;

import java.time.LocalDateTime;

public record TransactionEvent(String transactionId, double amount, LocalDateTime timestamp) {
}
