package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MemberRepository memberRepository;

    @GetMapping("/staff/dashboard")
    public String staffDashboard() {
        return "staff-dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        Account linkedAccount = accountRepository.findByMobileNumber(mobileNumber);
        List<Member> members = memberRepository.findByLinkedAccount(linkedAccount);
        if(members.size() != 0)
            model.addAttribute("members", members);
        return "user-dashboard1";
    }

    @GetMapping("/staff/trail")
    public String trail() {
        return "staff-dashboard";
    }


}
