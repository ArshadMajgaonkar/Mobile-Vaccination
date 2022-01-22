package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/user/login")
    public String getUserLoginPage(Model model) {
        model.addAttribute("showOtpForm", false);
        return "user-login";
    }

    @PostMapping("/user/login")
    public String getUserLoginData(@ModelAttribute("accountInstance") Account account, Model model) {
        Account fetchedAccount = accountRepository.findByMobileNumber(account.getMobileNumber());
        if(fetchedAccount != null) {
            // TODO: verify otp && update time
            System.out.println("Account exists. Username=" + fetchedAccount.getMobileNumber());
            model.addAttribute("accountInstance", account);
            /*fetchedAccount.setLastLogin(fetchedAccount.getCurrentLogin());
            fetchedAccount.setCurrentLogin(new Date());
            accountRepository.save(fetchedAccount);*/
        }
        else {
            model.addAttribute("accountInstance", account);
        }
        model.addAttribute("showOtpForm", true);
        return "user-login";
    }

    @PostMapping("/generate-otp")
    public String getOtp(@ModelAttribute("accountInstance") Account account, Model model) {
        System.out.println("Generate OTP for Mobile Number: " + account.getMobileNumber());
        Account fetchedAccount = accountRepository.findByMobileNumber(account.getMobileNumber());
        if(fetchedAccount != null) {
            System.out.println("Account exists. Username=" + fetchedAccount.getMobileNumber());
            model.addAttribute("showOtpForm", true);
        }
        else {
            //model.addAttribute("message", "")
            account.setOtp("4567");
            fetchedAccount = accountRepository.save(account);
            account.setOtp("");
            System.out.println("Creating Account: " + account.getMobileNumber());
        }
        // TODO: generate OTP if account is created or exist
        model.addAttribute("showOtpForm", true);
        model.addAttribute("accountInstance", account);
        return "user-login";
    }

    @GetMapping("/staff/login")
    public String getStaffLogin() {
        return "staff-login";
    }

    @PostMapping("/staff/login")
    public String staffLogin(Model model, @ModelAttribute("staffInstance") Staff staff) {
        Staff fetchedStaff = staffRepository.findByUsername(staff.getUsername());
        if(fetchedStaff != null){
            //TODO: handle positive login && update login time
            System.out.println("Account exists. Username=" + fetchedStaff.getUsername());
            model.addAttribute("staffInstance", staff);
            //return "redirect:/staff/dashboard";
        }
        return "staff-login";
    }

    @GetMapping("/logout-success")
    public String getUserLogoutPage() {
        return "logout";
    }

    @ModelAttribute("accountInstance")
    public Account getAccountInstance() {
        return new Account();
    }

    @ModelAttribute("staffInstance")
    public Staff getStaffInstance() {
        return new Staff();
    }
}
