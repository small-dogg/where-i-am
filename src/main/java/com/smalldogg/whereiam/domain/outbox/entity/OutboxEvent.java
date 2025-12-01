package com.smalldogg.whereiam.domain.outbox.entity;

import com.smalldogg.whereiam.domain.DomainType;
import com.smalldogg.whereiam.domain.outbox.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Enumerated(EnumType.STRING)
    private DomainType domain;
    private String topic;
    private String key;
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    private int retryCount;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastAttemptAt;

    public void markSent() {
        this.status = OutboxStatus.SENT;
        this.lastAttemptAt = LocalDateTime.now();
    }

    public void markFailed() {
        if (retryCount++ > 5) {
            this.status = OutboxStatus.FAILED;
            this.lastAttemptAt = LocalDateTime.now();
        }
    }
}
