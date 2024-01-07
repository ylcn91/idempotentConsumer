package com.doksanbir.transactionproducer.service;

import com.doksanbir.transactionproducer.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProducerService {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    @Scheduled(fixedRateString = "${schedule.rate.ms}")
    public void sendRandomTransactionsPeriodically() {
        Transaction transaction = generateRandomTransaction();
        sendTransaction(transaction);
    }

    void sendTransaction(Transaction transaction) {
        log.info("Sending transaction: {}", transaction);
        kafkaTemplate.send(topic, transaction.transactionId(), transaction);
    }

    Transaction generateRandomTransaction() {
        String transactionId = UUID.randomUUID().toString();
        double amount = ThreadLocalRandom.current().nextDouble(1, 1000);
        LocalDateTime timestamp = LocalDateTime.now();
        return new Transaction(transactionId, amount, timestamp);
    }
}