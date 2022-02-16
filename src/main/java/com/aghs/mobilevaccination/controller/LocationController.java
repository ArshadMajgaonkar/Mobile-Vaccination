package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.LocationDTO;
import com.aghs.mobilevaccination.data.repository.location.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/location")
public class LocationController {
    @Autowired
    StateRepository stateRepository;

    @GetMapping("/get-locations")
    public String getLocations(Model model, @ModelAttribute("locationDto") LocationDTO locationDto) {
        model.addAttribute("states", stateRepository.findAll());
        return "book-slot";
    }

    @ModelAttribute("locationDto")
    public LocationDTO getLocationDTO() {
        return new LocationDTO();
    }
}
