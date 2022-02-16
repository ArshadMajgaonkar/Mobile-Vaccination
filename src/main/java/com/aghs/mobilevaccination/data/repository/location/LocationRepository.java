package com.aghs.mobilevaccination.data.repository.location;

import com.aghs.mobilevaccination.data.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
