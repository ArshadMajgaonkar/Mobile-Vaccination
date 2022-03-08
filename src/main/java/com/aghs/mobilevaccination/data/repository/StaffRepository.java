package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.location.Centre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, String> {
    Staff findByUsername(String username);
    List<Staff> findByRoleAndCentreIn(AuthGroup role, List<Centre> centreList);
}
