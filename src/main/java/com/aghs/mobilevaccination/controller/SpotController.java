package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.PinCodeDto;
import com.aghs.mobilevaccination.data.dto.SlotDto;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.location.State;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.location.CityRepository;
import com.aghs.mobilevaccination.data.repository.location.DistrictRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.location.StateRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
    @Autowired private CityRepository cityRepository;
    @Autowired private DistrictRepository districtRepository;
    @Autowired private SpotRepository spotRepository;
    @Autowired private StateRepository stateRepository;
    @Autowired private VaccineDriveRepository driveRepository;

    @Value("${registration.add.day.ahead}") private int ADD_REGISTRATION_DAY_AHEAD;


    @GetMapping("/get-by-city")
    public String getByCity(Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(ADD_REGISTRATION_DAY_AHEAD+1);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("selectedDate", minRegistrationDate);
        model.addAttribute("states", stateRepository.findAll());
        return "slot-by-city";
    }

    @PostMapping("/get-by-city")
    public String postByCity(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
                             @ModelAttribute("cityDto") CityDto cityDto,
                             Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("selectedDate", selectedDate);
        LocalDate minRegistrationDate = LocalDate.now().plusDays(ADD_REGISTRATION_DAY_AHEAD+1);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("states", stateRepository.findAll());
        System.out.println(selectedDate);
        System.out.println(cityDto);
        City selectedCity = getCity(cityDto, model);
        if (selectedCity != null) {
            List<Spot> spots = spotRepository.findByCity(selectedCity);
            model.addAttribute("spots", spots.size()!=0 ? spots : null);
            // remaining slots
            List<VaccineDrive> drives = driveRepository.findByDriveDateAndCityAndStatus(
                    selectedDate,
                    selectedCity,
                    VaccineDriveStatus.BOOKING
            );
            List<SlotDto> slotDtoList = VaccineDrive.toDto(vaccinationRepository, drives);
            model.addAttribute("slotDtoList", slotDtoList);
        }
        else
            messages.add("Please select a state.");
        return "slot-by-city";
    }

    @GetMapping("get-by-pin-code")
    public String getByPinCode(Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("selectedDate", minRegistrationDate);
        return "slot-by-pin-code";
    }

    @PostMapping("get-by-pin-code")
    public String postByPinCode(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate selectedDate,
                                @ModelAttribute("pinCodeDto") PinCodeDto pinCodeDto,
                                Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(3);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        model.addAttribute("selectedDate", selectedDate);
        List<Spot> spots = spotRepository.findByPinCode(pinCodeDto.getPinCode());
        if(spots.size() > 0) {
            model.addAttribute("spots", spots);
            // remaining slots
            List<VaccineDrive> drives = driveRepository.findByDriveDateAndCityAndStatus(
                    selectedDate,
                    spots.get(0).getCity(),
                    VaccineDriveStatus.BOOKING
            );
            System.out.println(spots.get(0).getCity());
            List<SlotDto> slotDtoList = VaccineDrive.toDto(vaccinationRepository, drives);
            model.addAttribute("slotDtoList", slotDtoList);
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
