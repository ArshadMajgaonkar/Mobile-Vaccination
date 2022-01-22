package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findByUsername(String username);
}
