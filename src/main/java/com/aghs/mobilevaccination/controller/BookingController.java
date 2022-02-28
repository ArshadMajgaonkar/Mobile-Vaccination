package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class BookingController extends DefaultController{
    @Autowired
    MemberVaccinationRepository memberVaccinationRepository;

    @GetMapping("/user/book-slot-by-city")
    public String getSlotBookingPage(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "book-slot";
    }

    @PostMapping("/user/book-slot-by-city")
    public String postSlotBookingPage(@ModelAttribute() Date vaccinationDate,
                                      @ModelAttribute("spotDto")SpotDto spotDto,
                                      Model model) {
        Spot spot = getSpotDto(spotDto, model);
        if (spot != null) {
            MemberVaccination memberVaccination = new MemberVaccination();
            memberVaccination.setVaccinationSpot(spot);
            memberVaccination.setRegisteredAt(new Date());
            memberVaccination.setRecipient(new Member());
        }
        return "book-slot";
    }

}
