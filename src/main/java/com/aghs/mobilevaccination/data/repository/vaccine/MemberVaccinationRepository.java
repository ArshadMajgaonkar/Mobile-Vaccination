package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberVaccinationRepository extends JpaRepository<MemberVaccination, String> {
    List<MemberVaccination> findByVaccinationSpotAndSelectedDate(Spot VaccinationSpot, LocalDate selectedDate);
}
