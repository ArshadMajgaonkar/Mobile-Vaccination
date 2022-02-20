package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.auth.StaffDetails;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/staff/vaccine")
public class VaccineController {
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    VaccineRepository vaccineRepository;

    @GetMapping("/")
    public String listVaccine(Model model) {
        List<Vaccine> vaccines = vaccineRepository.findAll();
        model.addAttribute("vaccines", vaccines);
        return "list-vaccine";
    }

    @GetMapping("/add-vaccine")
    public String getAddVaccine() {
        return "add-vaccine";
    }

    @PostMapping("/add-vaccine")
    public String postAddVaccine(Model model, @ModelAttribute("vaccine")Vaccine vaccine) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof StaffDetails) {
            String username = ((StaffDetails) principal).getUsername();
            Staff staff = staffRepository.findByUsername(username);
            vaccine.setAddedBy(staff);
            messages.add("Vaccine added: " + vaccine.getName());
        } else {
            messages.add("Access Denied: No staff user found.");
        }
        return "add-vaccine";
    }

    @ModelAttribute("vaccineInstance")
    public Vaccine getDefaultVaccine() {
        return new Vaccine();
    }

}
