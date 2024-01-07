package com.doksanbir.transactionproducer.service;

import com.doksanbir.transactionproducer.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionProducerServiceTest {

    private TransactionProducerService transactionProducerService;

    @Mock
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    private final String testTopic = "test-topic";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionProducerService = new TransactionProducerService(kafkaTemplate);
        ReflectionTestUtils.setField(transactionProducerService, "topic", testTopic);
    }

    @Test
    void sendRandomTransactionsPeriodically() {
        transactionProducerService.sendRandomTransactionsPeriodically();

        verify(kafkaTemplate, times(1)).send(eq(testTopic), anyString(), any(Transaction.class));
    }

    @Test
    void sendTransaction() {
        Transaction transaction = new Transaction("123", 500.0, LocalDateTime.now());
        transactionProducerService.sendTransaction(transaction);

        verify(kafkaTemplate, times(1)).send(testTopic, transaction.transactionId(), transaction);
    }

    @Test
    void generateRandomTransaction() {
        Transaction transaction = transactionProducerService.generateRandomTransaction();

        assertNotNull(transaction);
        assertNotNull(transaction.transactionId());
        assertTrue(transaction.amount() >= 1 && transaction.amount() < 1000);
        assertNotNull(transaction.timestamp());

        // Check if the transaction ID is a valid UUID
        assertDoesNotThrow(() -> UUID.fromString(transaction.transactionId()));

        // Check if the timestamp is close to the current time (within a reasonable buffer 5 seconds)
        LocalDateTime now = LocalDateTime.now();
        assertTrue(now.minusSeconds(5).isBefore(transaction.timestamp()) &&
                now.plusSeconds(5).isAfter(transaction.timestamp()));
    }

}
