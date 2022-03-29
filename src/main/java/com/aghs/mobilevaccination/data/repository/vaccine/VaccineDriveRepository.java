package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineDriveRepository extends JpaRepository<VaccineDrive, Long> {
    List<VaccineDrive> findByCityAndStatusIn(City city, List<VaccineDriveStatus> statusList);

    List<VaccineDrive> findByDriveDateAndCity(LocalDate driveDate, City city);
    List<VaccineDrive> findByDriveDateAndCityAndStatusAndVaccineIn(LocalDate driveDate,
                                                                   City city,
                                                                   VaccineDriveStatus status,
                                                                   List<Vaccine> vaccineList
    );
    List<VaccineDrive> findByDriveDateAndCityAndStatus(LocalDate driveDate, City city, VaccineDriveStatus status);
    List<VaccineDrive> findByDriveDateAndCityAndStatusIn(LocalDate driveDate, City city, List<VaccineDriveStatus> statusList);
    VaccineDrive findByDriveDateAndStatusAndCityAndVaccine(LocalDate driveDate,
                                                           VaccineDriveStatus status,
                                                           City city,
                                                           Vaccine vaccine
    );
    List<VaccineDrive> findByStatusAndCity(VaccineDriveStatus status, City city);
}
