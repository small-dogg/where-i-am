package com.smalldogg.whereiam.domain.location.in;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SaveUserLocationParam {
    private Long userSeq; // todo: security 추가후 제거

    private Double latitude;
    private Double longitude;
    @NotNull(message = "위치 정보 생성일시를 입력해주세요.")
    private LocalDateTime createdAt;

    @AssertTrue(message = "위치 정보가 올바르지 않습니다.")
    boolean isValidLocation() {
        return latitude != null && longitude != null;
    }
}
