package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.PinCodeDto;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.location.State;
import com.aghs.mobilevaccination.data.repository.location.CityRepository;
import com.aghs.mobilevaccination.data.repository.location.DistrictRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.location.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/spot")
public class SpotController extends DefaultController {
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final SpotRepository spotRepository;

    @Autowired
    public SpotController(StateRepository stateRepository,
                          DistrictRepository districtRepository,
                          CityRepository cityRepository,
                          SpotRepository spotRepository) {
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
        this.spotRepository = spotRepository;
    }

    @GetMapping("/get-by-city")
    public String getByCity(Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("states", stateRepository.findAll());
        return "slot-by-city";
    }

    @PostMapping("/get-by-city")
    public String postByCity(Model model, @ModelAttribute("cityDto") CityDto cityDto) {
        List<String> messages = new ArrayList<>();
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("cityDto", cityDto);
        model.addAttribute("messages", messages);
        System.out.println(cityDto);
        City selectedCity = getCity(cityDto, model);
        if (selectedCity != null) {
            List<Spot> spots = spotRepository.findByCity(selectedCity);
            model.addAttribute("spots", spots.size()!=0 ? spots : null);
            // remaining slots
            model.addAttribute("remainingSlots", getRemainingSlot(selectedCity, LocalDate.now()));
        }
        else
            messages.add("Please select a state.");
        return "slot-by-city";
    }

    @GetMapping("get-by-pin-code")
    public String getByPinCode(Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        return "slot-by-pin-code";
    }

    @PostMapping("get-by-pin-code")
    public String postByPinCode(Model model, @ModelAttribute("pinCodeDto") PinCodeDto pinCodeDto) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        List<Spot> spots = spotRepository.findByPinCode(pinCodeDto.getPinCode());
        if(spots.size() > 0) {
            model.addAttribute("spots", spots);
            // remaining slots
            City city = spots.get(0).getCity();
            model.addAttribute("remainingSlots", getRemainingSlot(city, minRegistrationDate));
        }
        return "slot-by-pin-code";
    }

    @ModelAttribute("cityDto")
    public CityDto getSpotDTO() {
        return new CityDto();
    }

    @ModelAttribute("pinCodeDto")
    public PinCodeDto getPinCodeDto() {
        return new PinCodeDto();
    }
}
