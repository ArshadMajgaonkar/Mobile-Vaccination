package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private MemberVaccinationRepository vaccinationRepository;
    @Autowired
    private GeneralUserDetailService userService;

    @PostMapping("/user/add-member")
    public String addMember(@ModelAttribute("memberInstance")Member member, Model model) {
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

    @GetMapping("/user/member/{memberId}")
    public String getMemberProfile(@PathVariable("memberId") Long memberId, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        Account currentAccount = userService.getCurrentAccount();
        Member member = memberRepository.findById(memberId).orElse(null);
        System.out.println("Account" + currentAccount.getMobileNumber());
        if(member==null)
            messages.add("No such member exists.");
        else if(member.getLinkedAccount() != currentAccount)
            messages.add("No such member exists");
        else {
            model.addAttribute("member", member);
            List<MemberVaccination> appointments = vaccinationRepository.findByRecipientAndStatus(
                    member, VaccinationStatus.REGISTERED);
            /*TODO: add rejected or IN_CENTRED as well in appointments after finalizing flow of vaccine_drive*/
            model.addAttribute("appointments", appointments);
            List<MemberVaccination> vaccinations = vaccinationRepository.findByRecipientAndStatus(
                    member, VaccinationStatus.VACCINATED);
            model.addAttribute("vaccinations", vaccinations);
            System.out.println(vaccinations);
            return "member-profile";
        }
        return "user-dashboard1";
    }

    @ModelAttribute("memberInstance")
    public Member getDefaultMember() {
        return new Member();
    }
}
