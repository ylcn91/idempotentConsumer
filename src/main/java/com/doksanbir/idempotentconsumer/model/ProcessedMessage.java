package com.doksanbir.idempotentconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("processed_messages")
public class ProcessedMessage {

    @Id
    private String messageId;
    private LocalDateTime processedAt;
}


