package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.AuthGroup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MemberRepository memberRepository;

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
    public String userDashboard(Model model) {
        String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        Account linkedAccount = accountRepository.findByMobileNumber(mobileNumber);
        List<Member> members = memberRepository.findByLinkedAccount(linkedAccount);
        if(members.size() != 0)
            model.addAttribute("members", members);
        return "user-dashboard1";
    }


}
