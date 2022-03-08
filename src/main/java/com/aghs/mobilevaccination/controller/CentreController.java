package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreDto;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.location.*;
import com.aghs.mobilevaccination.data.repository.location.*;
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
public class CentreController {
    @Autowired
    CentreRepository centreRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    StateRepository stateRepository;
    @Autowired
    StaffUserDetailService staffService;

    @GetMapping("/centre")
    public String getCentres(Model model) {
        model.addAttribute("centres", centreRepository.findAll());
        return "list-centre";
    }

    @GetMapping("/staff/centre")
    public String getCentresForStaff(Model model) {
        model.addAttribute("centres", centreRepository.findAll());
        return "list-centre";
    }

    @GetMapping("/staff/centre/add")
    public String getAddCentre(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "add-centre";
    }

    @PostMapping("/staff/centre/add")
    public String postAddCentre(Model model, @ModelAttribute("centreDto") CentreDto centreDto) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        System.out.println(centreDto);
        model.addAttribute("states", stateRepository.findAll());
        if(centreDto.getStateName() != null) {
            State selectedState = stateRepository.findByName(centreDto.getStateName());
            model.addAttribute("districts", districtRepository.findByState(selectedState));
            if(centreDto.getDistrictId() != -1) {
                District selectedDistrict =
                        districtRepository.findByIdAndState(centreDto.getDistrictId(), selectedState);
                model.addAttribute("cities", cityRepository.findByDistrict(selectedDistrict));
                if(centreDto.getCityId() != -1) {
                    City selectedCity =
                            cityRepository.findByIdAndDistrict(centreDto.getCityId(), selectedDistrict);
                    model.addAttribute("spots", spotRepository.findByCity(selectedCity));
                    if(centreDto.isValid()) {
                        Spot selectedSpot = spotRepository.findById(centreDto.getSpotId()).orElse(null);
                        Staff currentStaff = staffService.getCurrentStaff();
                        Centre newCentre = Centre.fromDto(centreDto.getName(), selectedSpot, currentStaff);
                        centreRepository.save(newCentre);
                        messages.add("New Centre added.");
                        centreDto =getDefaultCentreDto();
                    }
                }
            }
        }
        model.addAttribute("centreDto", centreDto);
        return "add-centre";
    }

    @ModelAttribute("centreDto")
    public CentreDto getDefaultCentreDto() {
        return new CentreDto(null, null, -1, -1, -1);
    }
}
