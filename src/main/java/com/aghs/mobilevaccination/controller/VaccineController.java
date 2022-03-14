package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.auth.StaffDetails;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineCategory;
import com.aghs.mobilevaccination.data.repository.DiseaseRepository;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineCategoryRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/staff/vaccine")
public class VaccineController {
    @Autowired
    DiseaseRepository diseaseRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StaffUserDetailService staffService;
    @Autowired
    VaccineRepository vaccineRepository;
    @Autowired
    VaccineCategoryRepository vaccineCategoryRepository;

    @GetMapping("/")
    public String listVaccine(Model model) {
        List<Vaccine> vaccines = vaccineRepository.findAll();
        model.addAttribute("vaccines", vaccines);
        return "list-vaccine";
    }

    @GetMapping("/add-vaccine")
    public String getAddVaccine(Model model) {
        model.addAttribute("diseases", diseaseRepository.findAll());
        return "add-vaccine";
    }

    @PostMapping("/add-vaccine")
    public String postAddVaccine(@ModelAttribute("vaccine")Vaccine vaccine,
                                 @ModelAttribute("selectedDiseaseId")Long diseaseId,
                                 Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("diseases", diseaseRepository.findAll());
        Staff staff = staffService.getCurrentStaff();
        if(diseaseId==null)
            messages.add("Disease cannot be empty.");
        else if(staff != null) {
            vaccine.setOfDisease(diseaseRepository.findById(diseaseId).orElse(null));
            vaccine.setAddedBy(staff);
            vaccine.setAddedAt(new Date());
            vaccineRepository.save(vaccine);
            messages.add("Vaccine added: " + vaccine.getName());
        }
        else messages.add("Access Denied: No staff user found.");
        return "add-vaccine";
    }

    // Vaccine-category

    @GetMapping("/category")
    public String getVaccineCategoryList(Model model) {
        model.addAttribute("vaccineCategories", vaccineCategoryRepository.findAll());
        return "list-vaccine-category";
    }


    @GetMapping("/add-vaccine-category")
    public String getAddVaccineCategory(Model model) {
        model.addAttribute("vaccines", vaccineRepository.findAll());
        model.addAttribute("vaccineCategories", vaccineCategoryRepository.findAll());
        return "add-vaccine-category";
    }

    @PostMapping("/add-vaccine-category")
    public String postAddVaccineCategory(Model model,
                                         @ModelAttribute("vaccineCategoryInstance")VaccineCategory vaccineCategory) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        System.out.println(vaccineCategory.isMandatory());
        Staff staff = staffService.getCurrentStaff();
        if(staff != null) {
            vaccineCategory.setAddedBy(staff);
            vaccineCategory.setAddedAt(new Date());
            vaccineCategoryRepository.save(vaccineCategory);
            messages.add("New Vaccine Category added: " + vaccineCategory.getName());
            model.addAttribute("vaccineCategoryInstance", new VaccineCategory());
        }
        else messages.add("Access Denied: No staff user found.");
        model.addAttribute("vaccines", vaccineRepository.findAll());
        model.addAttribute("vaccineCategories", vaccineCategoryRepository.findAll());
        return "add-vaccine-category";
    }

    @ModelAttribute("vaccineInstance")
    public Vaccine getDefaultVaccine() {
        return new Vaccine();
    }

    @ModelAttribute("vaccineCategoryInstance")
    public VaccineCategory getDefaultVaccineCategory() {
        return new VaccineCategory();
    }

}
