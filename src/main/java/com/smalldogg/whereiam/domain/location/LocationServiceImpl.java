package com.smalldogg.whereiam.domain.location;

import com.smalldogg.whereiam.domain.location.command.SaveUserLocationCommand;
import com.smalldogg.whereiam.domain.location.entity.UserLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public void saveLocation(SaveUserLocationCommand command) {
        UserLocation userLocation = UserLocation.of(command);
        locationRepository.save(userLocation);
    }
}
