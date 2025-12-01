package com.smalldogg.whereiam.domain.location;

import com.smalldogg.whereiam.domain.location.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<UserLocation, Long> {
}
