package com.smalldogg.whereiam.domain.outbox.listener;

import com.smalldogg.whereiam.domain.outbox.OutboxEventRepository;
import com.smalldogg.whereiam.domain.outbox.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OutboxListener {

    private final OutboxEventRepository outboxEventRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OutboxEvent event) {
        outboxEventRepository.save(event);
    }
}
