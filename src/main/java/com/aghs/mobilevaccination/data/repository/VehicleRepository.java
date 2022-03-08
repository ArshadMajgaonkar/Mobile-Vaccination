package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    List<Vehicle> findByCentre(Centre centre);
    Vehicle findByRegistrationNumber(String registrationNumber);
}
