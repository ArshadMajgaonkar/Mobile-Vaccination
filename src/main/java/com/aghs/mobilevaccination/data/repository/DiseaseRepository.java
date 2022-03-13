package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
}
