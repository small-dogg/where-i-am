package com.smalldogg.whereiam.domain.location;

import com.smalldogg.whereiam.domain.location.command.SaveUserLocationCommand;

public interface LocationService {

    void saveLocation(SaveUserLocationCommand command);
}
