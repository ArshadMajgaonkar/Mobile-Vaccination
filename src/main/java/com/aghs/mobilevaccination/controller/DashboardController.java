package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController extends DefaultController{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberVaccinationRepository memberVaccinationRepository;
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private VaccineDriveRepository driveRepository;
    @Autowired
    private StaffUserDetailService staffService;

    @GetMapping("/staff/admin-dashboard")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
        LocalDate today = LocalDate.now();
        Staff admin = staffService.getCurrentStaff();
        model.addAttribute("admin", admin);
        City workCity = admin.getCentre().getSpot().getCity();
        List<VaccineDrive> drives = driveRepository.findByDriveDateAndCity(today, workCity);
        List<MemberVaccination> vaccinations = new ArrayList<>();
        drives.forEach(drive -> {
            vaccinations.addAll(vaccinationRepository.findByVaccineDrive(drive));
        });
        //List<MemberVaccination> vaccinations = getVaccinationByCityAndDate(workCity, today);
        model.addAttribute("selectedDate", today);
        CityDto workCityDto = workCity.toDto();
        model.addAttribute("cityDto", workCityDto);
        getCity(workCityDto, model);
        model.addAttribute("vaccinations", vaccinations);
        return "admin-dashboard";
    }

    @PostMapping("/staff/admin-dashboard")
    public String postAdminDashboard(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
                                     @ModelAttribute("cityDto") CityDto cityDto,
                                     Model model) {
        City workCity = getCity(cityDto, model);
        if(selectedDate!=null && workCity!=null) {
            List<VaccineDrive> drives = driveRepository.findByDriveDateAndCity(selectedDate, workCity);
            List<MemberVaccination> vaccinations = new ArrayList<>();
            drives.forEach(drive -> {
                vaccinations.addAll(vaccinationRepository.findByVaccineDrive(drive));
            });
            //List<MemberVaccination> vaccinations = getVaccinationByCityAndDate(workCity, selectedDate);
            model.addAttribute("vaccinations", vaccinations);
            model.addAttribute("selectedDate", selectedDate);
        }
        Staff admin = staffService.getCurrentStaff();
        model.addAttribute("admin", admin);
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
