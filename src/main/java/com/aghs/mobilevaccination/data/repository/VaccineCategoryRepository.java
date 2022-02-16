package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.VaccineCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineCategoryRepository extends JpaRepository<VaccineCategory, String> {
}
