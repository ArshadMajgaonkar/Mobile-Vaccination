package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.AuthGroup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class DashboardController {

    @GetMapping("/staff/admin-dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/staff/dashboard")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public String staffDashboard() {
        for(GrantedAuthority authority: SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_" + AuthGroup.ADMIN.toString()))
                return "redirect:/staff/admin-dashboard";
        }
        return "staff-dashboard";
    }

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userDashboard() {
        return "user-dashboard1";
    }


}
