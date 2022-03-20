package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

class VaccinationDto{
    private Integer vaccinationId;
    private String staffUsername;

    public Integer getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(Integer vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    @Override
    public String toString() {
        return "VaccinationDto{" +
                "vaccinationId=" + vaccinationId +
                ", staffUsername='" + staffUsername + '\'' +
                '}';
    }
}

@RestController
public class VaccinationRestController {
    @Autowired
    MemberVaccinationRepository vaccinationRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    VaccineDriveRepository driveRepository;

    @PostMapping("/staff/update-vaccination-status")
    public ResponseEntity<String> updateStatus(@RequestBody VaccinationDto dto) {
        System.out.println(dto);
        String vaccinationId = dto.getVaccinationId().toString();
        MemberVaccination vaccination = vaccinationRepository.findById(Long.valueOf(dto.getVaccinationId()))
                .orElse(null);
        if(vaccination!=null){
            // TODO: update vaccineDrive if null
            /*if(vaccination.getVaccineDrive()==null) {
                City city = staffRepository.findByUsername(dto.getStaffUsername()).getCentre().getSpot().getCity();
                VaccineDrive drive = driveRepository.findByDriveDateAndStatusAndCityAndVaccine(
                        LocalDate.now(),
                        VaccineDriveStatus.ON_GOING,
                        city,
                        vaccination.getVaccineCategory().getVaccine()
                );
                vaccination.setVaccineDrive(drive);
            }*/
            if(vaccination.getStatus() == VaccinationStatus.VACCINATED ||
                    vaccination.getStatus() == VaccinationStatus.DISCARDED) {
                String message = "Status already update for " + vaccinationId;
                return new ResponseEntity<String>(message, HttpStatus.ALREADY_REPORTED);
            }
            vaccination.setStatus(VaccinationStatus.VACCINATED);
            vaccination.setVaccinatedAt(new Date());
            vaccinationRepository.save(vaccination);
            String message = String.format("Status for %s: VACCINATED", vaccinationId);
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }
        else {
            String message = "No registration for " + vaccinationId;
            return  new ResponseEntity<String>(message, HttpStatus.NOT_FOUND);
        }
    }
}
