package com.smalldogg.whereiam.domain.outbox;

import com.smalldogg.whereiam.domain.outbox.entity.OutboxEvent;
import com.smalldogg.whereiam.domain.outbox.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 1000L)
    public void relay() {
        List<OutboxEvent> events = outboxEventRepository.findTop100ByStatusOrderBySeqAsc(OutboxStatus.PENDING);

        log.info("event producing [count:{}]", events.size());

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload()).get();
                event.markSent();
            } catch (Exception e) {
                event.markFailed();
            }
        }
    }
}
