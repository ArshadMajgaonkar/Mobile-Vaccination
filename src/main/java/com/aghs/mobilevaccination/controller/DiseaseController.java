package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Disease;
import com.aghs.mobilevaccination.data.repository.DiseaseRepository;
import com.aghs.mobilevaccination.service.StaffUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/staff/disease")
public class DiseaseController {
    @Autowired
    DiseaseRepository diseaseRepository;
    @Autowired
    StaffUserDetailService staffService;

    @GetMapping({"", "/"})
    public String getDiseaseList(Model model) {
        model.addAttribute("diseases", diseaseRepository.findAll());
        return "list-disease";
    }

    @GetMapping("/add")
    public String getAddDisease() {
        return "add-disease";
    }

    @PostMapping("/add")
    public String postAddDisease(@ModelAttribute("diseaseName")String diseaseName, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        if(diseaseName!=null && !diseaseName.equals("")){
            Disease disease = new Disease();
            disease.setName(diseaseName);
            disease.setAddedAt(new Date());
            System.out.println(disease.getName());
            disease.setAddedBy(staffService.getCurrentStaff());
            diseaseRepository.save(disease);
            disease = new Disease();
            messages.add("New disease added.");
        }
        else
            messages.add("Disease Name cannot be null");
        return "add-disease";
    }

    @ModelAttribute("disease")
    public Disease getDisease() {
        return new Disease();
    }

}
