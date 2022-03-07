package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineDriveRepository extends JpaRepository<VaccineDrive, Long> {
    List<VaccineDrive> findByStatusAndCity(VaccineDriveStatus status, City city);
    List<VaccineDrive> findByDriveDateAndCity(LocalDate driveDate, City city);
    List<VaccineDrive> findByDriveDateAndStatusAndCity(LocalDate driveDate, VaccineDriveStatus status, City city);
}
