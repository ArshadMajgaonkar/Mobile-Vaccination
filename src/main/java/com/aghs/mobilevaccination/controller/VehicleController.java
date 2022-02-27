package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreSelectDto;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.*;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
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
@RequestMapping("/staff/vehicle")
public class VehicleController extends DefaultController{
    @Autowired
    private CentreRepository centreRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private StaffUserDetailService staffService;


    @GetMapping(value = {"", "/"})
    public String getVehicle(Model model) {
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "list-vehicle";
    }

    @GetMapping("/add")
    public String getAddVehicle(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "add-vehicle";
    }

    @PostMapping("/add")
    public String postAddVehicle(Model model,
                                 @ModelAttribute("vehicle")Vehicle vehicle,
                                 @ModelAttribute("centreSelectDto")CentreSelectDto centreSelectDto) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        Centre selectedCentre = getCentre(centreSelectDto, model);
        if(selectedCentre != null) {
            if (vehicle.getRegistrationNumber() != null && !vehicle.getRegistrationNumber().strip().equals("")) {
                vehicle.setCentre(selectedCentre);
                vehicle.setAddedAt(new Date());
                vehicle.setAddedBy(staffService.getCurrentStaff());
                vehicleRepository.save(vehicle);
                messages.add("New vehicle added.");
                centreSelectDto = new CentreSelectDto(null, -1, -1, -1);
                vehicle = new Vehicle();
            } else {
                model.addAttribute("vehicle", vehicle);
                messages.add("Enter a Valid Registration Number");
            }
        }
        model.addAttribute("centreSelectDto", centreSelectDto);
        model.addAttribute("vehicle", vehicle);
        return "add-vehicle";
    }



    @ModelAttribute("vehicle")
    public Vehicle getDefaultVehicle() {
        return new Vehicle();
    }
}
