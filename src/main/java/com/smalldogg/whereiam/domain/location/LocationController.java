package com.smalldogg.whereiam.domain.location;

import com.smalldogg.whereiam.domain.location.command.SaveUserLocationCommand;
import com.smalldogg.whereiam.domain.location.in.SaveUserLocationParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
@RestController
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    void save(@RequestBody @Valid SaveUserLocationParam param) {
        locationService.saveLocation(
                new SaveUserLocationCommand(
                        param.getUserSeq(),
                        param.getLatitude(),
                        param.getLongitude(),
                        param.getCreatedAt()
                )
        );
    }

}
