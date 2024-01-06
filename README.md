# Transaction Consumer Service

## Overview
This service consumes Kafka messages, processes them idempotently, and makes REST API calls to update account balances.

## Key Components
- `TransactionConsumerService`: Listens and processes Kafka messages.
- `ProcessedMessageRepository`: Checks and inserts processed messages to ensure idempotent processing.

## Functionality
- Consumes and processes Kafka messages.
- Updates account balances via REST API calls.
- Implements idempotent processing for handling duplicate messages.

## Idempotent Consumer Pattern
The service uses the Idempotent Consumer pattern, crucial for applications with at-least-once delivery guarantees. It ensures consistent outcomes even when processing duplicate messages. The service achieves idempotency by recording processed message IDs in the database, allowing detection and discarding of duplicates.

For more details, visit [Idempotent Consumer Pattern on Microservices.io](https://microservices.io/patterns/communication-style/idempotent-consumer.html).

## Configuration
Configurable Kafka topic, consumer group ID, and other consumer settings.
