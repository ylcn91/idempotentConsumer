package com.doksanbir.idempotentconsumer.consumer;

import com.doksanbir.idempotentconsumer.model.ProcessedMessage;
import com.doksanbir.idempotentconsumer.model.TransactionEvent;
import com.doksanbir.idempotentconsumer.repository.ProcessedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@Service
public class TransactionConsumerService {

    private final TransactionEventConverter eventConverter;
    private final ProcessedMessageRepository processedMessageRepository;

    private final RestClient restClient;

    public TransactionConsumerService(RestClient.Builder builder, TransactionEventConverter eventConverter, ProcessedMessageRepository processedMessageRepository) {
        this.eventConverter = eventConverter;
        this.processedMessageRepository = processedMessageRepository;
        this.restClient = builder.baseUrl("http://localhost:8082").build();

    }

    @KafkaListener(topics = "${kafka.topic}")
    public void listen(String message) {
        log.info("Received message: {}", message);
        TransactionEvent event = eventConverter.convertToTransactionEvent(message);

        checkAndProcessMessage(event)
                .subscribe(this::logResult, error -> logError(event, error));
    }

    private Mono<ProcessedMessage> checkAndProcessMessage(TransactionEvent event) {
        return processedMessageRepository.findById(event.transactionId())
                .switchIfEmpty(processMessage(event));
    }

    private Mono<ProcessedMessage> processMessage(TransactionEvent event) {
        return updateAccountBalance(event)
                .then(insertProcessedMessage(event))
                .doOnSuccess(processedMessage -> logProcessingSuccess(event));
    }

    //TODO: add a request body to the put request as record instead of a string
    private Mono<Void> updateAccountBalance(TransactionEvent event) {
        String requestBody = "{\"amount\":" + event.amount() + "}";

        return Mono.fromCallable(() -> restClient
                        .put()
                        .uri("/accounts/{transactionId}/balance", event.transactionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .toBodilessEntity())
                .doOnSuccess(response -> log.info("REST call response status: {}", response.getStatusCode()))
                .then();
    }


    private Mono<ProcessedMessage> insertProcessedMessage(TransactionEvent event) {
        return processedMessageRepository
                .insertMessage(event.transactionId(), LocalDateTime.now());
    }

    private void logResult(ProcessedMessage result) {
        if (result != null) {
            log.info("Processed new message: {}", result.getMessageId());
        }
    }

    private void logProcessingSuccess(TransactionEvent event) {
        log.info("Inserted new message ID: {}", event.transactionId());
    }

    private void logError(TransactionEvent event, Throwable error) {
        log.error("Error processing message: {}", event.transactionId(), error);
    }
}

