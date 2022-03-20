package com.aghs.mobilevaccination.data.repository.vaccine;

import com.aghs.mobilevaccination.data.model.Disease;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, String> {
    List<Vaccine> findByOfDisease(Disease ofDisease);
    Vaccine findByName(String name);
}
