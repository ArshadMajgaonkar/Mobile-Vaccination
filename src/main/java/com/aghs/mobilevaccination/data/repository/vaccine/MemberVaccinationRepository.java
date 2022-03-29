package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberVaccinationRepository extends JpaRepository<MemberVaccination, Long> {

    List<MemberVaccination> findByVaccineDriveAndStatus(VaccineDrive vaccineDrive, VaccinationStatus status);

    List<MemberVaccination> findByRecipient(Member recipient);
    List<MemberVaccination> findByVaccineCategoryAndRecipient(VaccineCategory vaccineCategory, Member recipient);
    List<MemberVaccination> findByRecipientAndStatusOrderByVaccinatedAt(Member recipient, VaccinationStatus status);
    List<MemberVaccination> findByRecipientAndStatus(Member recipient, VaccinationStatus status);


    List<MemberVaccination> findByVaccinationSpotAndVaccineDrive(Spot VaccinationSpot,
                                                                 VaccineDrive vaccineDrive);

    List<MemberVaccination> findByVaccineDrive(VaccineDrive vaccineDrive);
    List<MemberVaccination> findByVaccineDriveAndVaccinationSpotAndStatus(VaccineDrive vaccineDrive,
                                                                          Spot VaccinationSpot,
                                                                          VaccinationStatus status);
}
