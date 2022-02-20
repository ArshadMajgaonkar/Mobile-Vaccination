package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.LocationDTO;
import com.aghs.mobilevaccination.data.dto.PinCodeDto;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import com.aghs.mobilevaccination.data.model.location.Location;
import com.aghs.mobilevaccination.data.model.location.State;
import com.aghs.mobilevaccination.data.repository.location.CityRepository;
import com.aghs.mobilevaccination.data.repository.location.DistrictRepository;
import com.aghs.mobilevaccination.data.repository.location.LocationRepository;
import com.aghs.mobilevaccination.data.repository.location.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/location")
public class LocationController {
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public LocationController(StateRepository stateRepository,
                              DistrictRepository districtRepository,
                              CityRepository cityRepository,
                              LocationRepository locationRepository) {
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/get-by-city")
    public String getByLocation(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "slot-by-city";
    }

    @PostMapping("/get-by-city")
    public String postByLocation(Model model, @ModelAttribute("locationDto") LocationDTO locationDto) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("locationDto", locationDto);
        model.addAttribute("messages", messages);
        if(locationDto.getState() != null) {
            State selectedState = stateRepository.findByName(locationDto.getState());
            List<District> districts = districtRepository.findByState(selectedState);
            model.addAttribute("districts", districts);
            districts.forEach(System.out::println);
            if (locationDto.getDistrict() != null ) {
                District selectedDistrict = districtRepository.findByNameAndState(locationDto.getDistrict(), selectedState);
                List<City> cities = cityRepository.findByDistrict(selectedDistrict);
                model.addAttribute("cities", cities);
                cities.forEach(System.out::println);
                if (locationDto.getCity() != null) {
                    City selectedCity = cityRepository.findByNameAndDistrict(locationDto.getCity(), selectedDistrict);
                    List<Location> locations = locationRepository.findByCity(selectedCity);
                    System.out.println(locations);
                    model.addAttribute("locations", locations.size()!=0 ? locations : null);
                }
            }
        }
        else {
            messages.add("Please select a state.");
        }
        return "slot-by-city";
    }

    @GetMapping("get-by-pin-code")
    public String getByPinCode(Model model) {
        return "slot-by-pin-code";
    }

    @PostMapping("get-by-pin-code")
    public String postByPinCode(Model model, @ModelAttribute("pinCodeDto") PinCodeDto pinCodeDto) {
        List<Location> locations = locationRepository.findByPinCode(pinCodeDto.getPinCode());
        model.addAttribute("locations", locations.size()!=0 ? locations : null);
        return "slot-by-pin-code";
    }

    @ModelAttribute("locationDto")
    public LocationDTO getLocationDTO() {
        return new LocationDTO();
    }

    @ModelAttribute("pinCodeDto")
    public PinCodeDto getPinCodeDto() {
        return new PinCodeDto();
    }
}
