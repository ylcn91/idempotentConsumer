package com.doksanbir.transactionproducer.model;

import java.time.LocalDateTime;

public record Transaction(String transactionId, double amount, LocalDateTime timestamp) {
}

