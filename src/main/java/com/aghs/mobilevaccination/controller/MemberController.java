package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MemberController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/user/add-member")
    public String addMember(@ModelAttribute("memberInstance")Member member, Model model) {
        // TODO: Member Validation with proper message
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        if( !member.isAadharIdValid()) {
            messages.add("Aadhar number is not valid");
        }
        else if( !member.isFullNameValid()) {
            messages.add("Full name is not valid");
        }
        else if( !member.isBirthYearValid()) {
            messages.add("Birth Year is not valid");
        }
        else {
            String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();
            Account linkedAccount = accountRepository.findByMobileNumber(mobileNumber);
            member.setLinkedAccount(linkedAccount);
            member.processData();
            try{
                memberRepository.save(member);
                messages.add("New Member added.");
            }
            catch(Exception e){
                messages.add("Member already exist with same Aaddhar Number.");
            }
            model.addAttribute("members", memberRepository.findByLinkedAccount(linkedAccount));
        }
        return "user-dashboard1";
    }

    @ModelAttribute("memberInstance")
    public Member getDefaultMember() {
        return new Member();
    }
}
