package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberVaccinationRepository extends JpaRepository<MemberVaccination, String> {
}
