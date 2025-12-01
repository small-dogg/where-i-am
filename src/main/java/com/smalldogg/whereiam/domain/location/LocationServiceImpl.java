package com.smalldogg.whereiam.domain.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.whereiam.domain.DomainType;
import com.smalldogg.whereiam.domain.location.command.SaveUserLocationCommand;
import com.smalldogg.whereiam.domain.location.entity.UserLocation;
import com.smalldogg.whereiam.domain.outbox.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final ObjectMapper objectMapper;
    private final LocationRepository locationRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void saveLocation(SaveUserLocationCommand command) {
        UserLocation userLocation = UserLocation.of(command);
        locationRepository.save(userLocation);

        try {
            String payload = toPayload(userLocation);

            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setDomain(DomainType.LOCATION);
            outboxEvent.setTopic("location-events");
            outboxEvent.setKey(String.valueOf(userLocation.getUserSeq()));
            outboxEvent.setPayload(payload);
            outboxEvent.setRetryCount(0);
            applicationEventPublisher.publishEvent(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String toPayload(UserLocation userLocation) throws JsonProcessingException {
        return objectMapper.writeValueAsString(userLocation);
    }
}
