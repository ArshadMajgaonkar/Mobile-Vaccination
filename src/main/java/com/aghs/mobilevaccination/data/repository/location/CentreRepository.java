package com.aghs.mobilevaccination.data.repository.location;

import com.aghs.mobilevaccination.data.model.location.Centre;
import com.aghs.mobilevaccination.data.model.location.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentreRepository extends JpaRepository<Centre, Long> {
    List<Centre> findBySpot(Spot spot);
}
