package com.smalldogg.whereiam.domain.location.entity;

import com.smalldogg.whereiam.domain.location.command.SaveUserLocationCommand;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long userSeq;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    public static UserLocation of(SaveUserLocationCommand command) {
        UserLocation userLocation = new UserLocation();
        userLocation.setUserSeq(command.getUserSeq());
        userLocation.setLatitude(command.getLatitude());
        userLocation.setLongitude(command.getLongitude());
        userLocation.setCreatedAt(command.getCreatedAt());
        return userLocation;
    }
}
