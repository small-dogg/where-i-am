package com.smalldogg.whereiam.event.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocationEventProducer {

    private static final String TOPIC = "location-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(Long userSeq, String payload) {
        kafkaTemplate.executeInTransaction(operations -> {
            log.info("TX START - userSeq={}, payloads={}", userSeq, payload);

            operations.send(TOPIC, String.valueOf(userSeq), payload);

            log.info("TX END");
            return null;
        });
    }
}
