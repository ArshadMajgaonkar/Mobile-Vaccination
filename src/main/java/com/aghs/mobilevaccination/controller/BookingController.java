package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController extends DefaultController{
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberVaccinationRepository memberVaccinationRepository;
    @Autowired
    GeneralUserDetailService userService;

    @PostMapping("/user/member/show-slot")
    public String getSlotBookingPage(@RequestBody Long userId, Model model) {
        model.addAttribute("states", stateRepository.findAll());
        LocalDate today = LocalDate.now();
        model.addAttribute("selectedDate", today);
        model.addAttribute("remainingSlots", "N/A");
        return "book-slot-by-city";
    }

    @PostMapping("/user/member/get-slot-by-city")
    public String postSlotBookingPage(@RequestBody LocalDate selectedDate,
                                      @RequestBody CityDto cityDto,
                                      Model model) {
        City city = getCity(cityDto, model);
        if(city != null) {
            model.addAttribute("spots", new Spot());
        }
        return "book-slot-by-city";
    }

    @PostMapping("/user/member/book-slot")
    public String bookSlot(@RequestBody Long userId,
                           @RequestBody LocalDate selectedDate,
                           @RequestBody SpotDto spotDto,
                           Model model) {
        City city = new City();
        List<Spot> spots = new ArrayList<>();
        model.addAttribute("reamainingSlots", "N/A");
        Spot selectedSpot = getSpotWithSelectedCityAndItsSpots(spotDto, model, city, spots);
        if(city.getName() != null) {
            Long bookedSlot = City.getRemainingSlots(memberVaccinationRepository, selectedDate, spots);
            Long remainingSlots = city.getAllotedSlotsPerDay() - bookedSlot;
            model.addAttribute("remainingSlots", remainingSlots);
        }
        if (selectedSpot != null) {
            Member currentMember = memberRepository.findById(userId).orElse(null);
            MemberVaccination memberVaccination = new MemberVaccination();
            memberVaccination.setVaccinationSpot(selectedSpot);
            memberVaccination.setRegisteredAt(new Date());
            memberVaccination.setRecipient(currentMember);
            memberVaccinationRepository.save(memberVaccination);
        }
        return "book-slot";
    }

}
