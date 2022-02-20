package com.aghs.mobilevaccination.data.repository.location;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByDistrict(District district);
    City findByNameAndDistrict(String name, District district);

}
