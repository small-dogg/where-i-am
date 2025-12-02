package com.smalldogg.whereiam.domain.outbox;

import com.smalldogg.whereiam.domain.outbox.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxRelayService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void relay(OutboxEvent event) {
        try {
            kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload()).get();
            event.markSent();
        } catch (Exception e) {
            event.markFailed();
        }
    }
}
