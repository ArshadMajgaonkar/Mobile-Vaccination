package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/staff/vehicle")
public class VehicleController {
    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("")
    public String getVehicle(Model model) {
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "list-vehicle";
    }

    @GetMapping("/add")
    public String getAddVehicle() {
        return "add-vehicle";
    }

    @PostMapping("/add")
    public String postAddVehicle(Model model, @ModelAttribute("vehicleInstance")Vehicle vehicle) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        if(vehicle.getRegistrationNumber()!=null && !vehicle.getRegistrationNumber().equals("")) {
            vehicleRepository.save(vehicle);
            messages.add("New vehicle added.");
        }
        else {
            model.addAttribute("vehicleInstance", vehicle);
            messages.add("Enter a Valid Registration Number");
        }
        return "add-vehicle";
    }

    @ModelAttribute("vehicleInstance")
    public Vehicle getDefaultVehicle() {
        return new Vehicle();
    }
}
