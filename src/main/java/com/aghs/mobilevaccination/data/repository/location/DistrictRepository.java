package com.aghs.mobilevaccination.data.repository.location;

import com.aghs.mobilevaccination.data.model.location.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Integer> {
}
