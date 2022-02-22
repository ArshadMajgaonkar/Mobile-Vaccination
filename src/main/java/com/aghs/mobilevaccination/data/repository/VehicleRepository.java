package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
