package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import com.aghs.mobilevaccination.service.TwilioMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private TwilioMessagingService messagingService;
    @Autowired
    private GeneralUserDetailService generalUserService;
    @Autowired
    private StaffUserDetailService staffService;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/user/login")
    @PreAuthorize("not isAuthenticated()")
    public String getUserLoginPage(Model model) {
        model.addAttribute("showOtpForm", false);
        return "index";
    }

    @PostMapping("/user/process-login")
    public String getUserLoginData(@ModelAttribute("accountInstance") Account account, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("accountInstance", account);
        model.addAttribute("showOtpForm", false);
        Account fetchedAccount = accountRepository.findByMobileNumber(account.getMobileNumber());
        if(fetchedAccount == null)
            messages.add("No account exist with " + account.getMobileNumber());
        else if( !account.isOtpValid()) {
            messages.add("OTP is invalid.");
            model.addAttribute("showOtpForm", true);
        }
        else if(fetchedAccount.isOtpExpired())
            messages.add("OTP Expired!");
        else {
            System.out.println("Account exists. Username=" + fetchedAccount.getMobileNumber());
            generalUserService.updateLastLogin(fetchedAccount);
            return "redirect:/user/dashboard";
        }
        return "index";
    }

    @PostMapping("/generate-otp")
    public String getOtp(@ModelAttribute("accountInstance") Account account, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("accountInstance", account);
        if(account.isMobileNumberValid()) {
            //account.addCountryCodeToMobileNumberIfNotPresent();
            Account fetchedAccount = accountRepository.findByMobileNumber(account.getMobileNumber());
            if(fetchedAccount == null){
                fetchedAccount = accountRepository.save(account);
                messages.add("New account created for " + account.getMobileNumber());
                System.out.println("Creating Account: " + account.getMobileNumber());
            }
            fetchedAccount.generateOtp();
            String errorMessage = messagingService.sendOtpMessage(fetchedAccount.getMobileNumber(), fetchedAccount.getOtp());
            if(errorMessage != null)
                messages.add(errorMessage);
            else {                                                               // Saves OTP in the database
                generalUserService.updateOtp(fetchedAccount);
                model.addAttribute("showOtpForm", true);
                messages.add("OTP sent successfully");
                System.out.println("OTP: " + fetchedAccount.getOtp());
            }
        }
        else {
            messages.add("Mobile Number cannot be empty.");
            model.addAttribute("showOtpForm", false);
        }
        return "index";
    }

    @GetMapping("/staff/login")
    @PreAuthorize("not isAuthenticated()")
    public String getStaffLogin() {
        return "staff-login";
    }

    @PostMapping("/staff/process-login")
    public String staffLogin(Model model, @ModelAttribute("staffInstance") Staff staff) {
        Staff fetchedStaff = staffRepository.findByUsername(staff.getUsername());
        if(fetchedStaff != null){
            System.out.println("Account exists. Username=" + fetchedStaff.getUsername());
            fetchedStaff = staffService.updateCurrentAndLastLogin(fetchedStaff);
            model.addAttribute("staffInstance", staff);
            System.out.println("Current & Last login updated");
            return "redirect:/staff/dashboard";
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
