package com.aghs.mobilevaccination.service;

import com.aghs.mobilevaccination.auth.StaffDetails;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StaffUserDetailService implements UserDetailsService {
    private final StaffRepository staffRepository;

    public StaffUserDetailService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = this.staffRepository.findByUsername(username);
        if (staff == null) {
            throw new UsernameNotFoundException("No staff by username: " + username);
        }
        System.out.println("Staff fetched in Service: " + staff.getUsername());
        return new StaffDetails(staff);
    }

    public Staff updateCurrentAndLastLogin(Staff staff) {
        staff.setLastLogin(staff.getCurrentLogin());
        staff.setCurrentLogin(new Date());
        return staffRepository.save(staff);
    }
}
