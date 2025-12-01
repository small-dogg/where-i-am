package com.smalldogg.whereiam.domain.location.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SaveUserLocationCommand {
    private Long userSeq;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
