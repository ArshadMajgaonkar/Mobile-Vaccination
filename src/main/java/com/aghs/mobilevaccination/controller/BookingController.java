package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.SlotDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.DiseaseRepository;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import com.twilio.rest.preview.bulkExports.export.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController extends DefaultController{
    @Autowired
    DiseaseRepository diseaseRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberVaccinationRepository memberVaccinationRepository;
    @Autowired
    VaccineRepository vaccineRepository;
    @Autowired
    VaccineDriveRepository driveRepository;
    @Autowired
    GeneralUserDetailService userService;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${registration.add.day.ahead}") private Integer ADD_REGISTRATION_DAY_AHEAD;

    private LocalDate addMinRegistrationDate(Model model) {
        LocalDate minRegistrationDate = LocalDate.now().plusDays(ADD_REGISTRATION_DAY_AHEAD+1);
        model.addAttribute("minRegistrationDate", minRegistrationDate);
        return  minRegistrationDate;
    }

    @PostMapping("/user/member/to-check-slot")
    public String getSlotBookingPage(@ModelAttribute("memberId") Long memberId, Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        addMinRegistrationDate(model);
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member!=null) {
            if(!member.hasCompletedVaccinationInterval(vaccinationRepository))
                messages.add("Please complete Interval Period of Previous Vaccination.");
            model.addAttribute("states", stateRepository.findAll());
            LocalDate minRegistrationDate = addMinRegistrationDate(model);
            model.addAttribute("selectedDate", minRegistrationDate);
            model.addAttribute("memberName", member.getFullName());
        } else {
            messages.add("No such Member Exist.");
        }
        return "book-slot-by-city";
    }

    @PostMapping("/user/member/check-slot")
    public String postSlotBookingPage(@ModelAttribute("memberId") Long memberId,
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate selectedDate,
                                      @ModelAttribute("cityDto")CityDto cityDto,
                                      Model model) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        addMinRegistrationDate(model);
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member!=null) {
            City selectedCity = getCity(cityDto, model);
            if(selectedDate==null)
                messages.add("Please select the date");
            if(selectedCity != null) {
                List<Vaccine> eligibleVaccines = member.getEligibleVaccines(
                        diseaseRepository,
                        vaccinationRepository,
                        vaccineRepository
                );
                List<VaccineDrive> drives = driveRepository.findByDriveDateAndCityAndStatusAndVaccineIn(
                        selectedDate,
                        selectedCity,
                        VaccineDriveStatus.BOOKING,
                        eligibleVaccines
                );
                List<SlotDto> slotDtoList = VaccineDrive.toDto(vaccinationRepository, drives);
                model.addAttribute("slotDtoList", slotDtoList);
                model.addAttribute("spots", spotRepository.findByCity(selectedCity));
            } else
                messages.add("Please select the city.");
            model.addAttribute("memberName", member.getFullName());
        } else
            messages.add("No such Member Exist.");
        model.addAttribute("selectedDate", selectedDate);
        return "book-slot-by-city";

    }

    @PostMapping("/user/member/book-slot")
    public String bookSlot(@ModelAttribute("memberId") Long memberId,
                           @ModelAttribute("driveId") Long driveId,
                           @ModelAttribute("spotId") Long spotId,
                           Model model) {
        List<String> messages = new ArrayList<>();
        // data extraction
        City city = new City();
        List<Spot> spots = new ArrayList<>();
        // data checking
        Member currentMember = memberRepository.findById(memberId).orElse(null);
        Spot selectedSpot = spotRepository.findById(spotId).orElse(null);
        VaccineDrive drive = driveRepository.findById(driveId).orElse(null);
        if(currentMember == null)
            messages.add("No such Member exist: " + String.valueOf(memberId));
        else if(drive==null)
            messages.add("No such Vaccine Drive exists.");
        else if(!currentMember.hasCompletedVaccinationInterval(memberVaccinationRepository)) {
            messages.add("Please complete Interval Period of Previous Vaccination.");
        }
        else if(selectedSpot != null) {
            currentMember.discardUnvaccinatedRegistration(vaccinationRepository);
            Long remainingSlots = drive.getRemainingSlot(vaccinationRepository);
            if(remainingSlots <= 0)
                messages.add("All slots are booked for selected city.");
            else {
                MemberVaccination memberVaccination = new MemberVaccination();
                memberVaccination.setRegisteredAt(new Date());
                memberVaccination.setRecipient(currentMember);
                memberVaccination.setVaccinationSpot(selectedSpot);
                memberVaccination.setVaccineDrive(drive);
                memberVaccinationRepository.save(memberVaccination);
                messages.add("Vaccination Slot Booked");
            }
            model.addAttribute("remainingSlots", remainingSlots);
            System.out.print("RemainingSlot");
            System.out.println(remainingSlots);
            model.addAttribute("cityDto", selectedSpot.getCity().toDto());
            model.addAttribute("selectedDate", drive.getDriveDate());
            model.addAttribute("memberName", currentMember.getFullName());
        }
        addMinRegistrationDate(model);
        model.addAttribute("messages", messages);
        model.addAttribute("states", stateRepository.findAll());
        return "book-slot-by-city";
    }

    @ModelAttribute("spotDto")
    public SpotDto getDefaultSpotDto() {
        return new SpotDto();
    }
}
