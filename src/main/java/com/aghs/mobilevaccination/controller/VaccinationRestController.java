package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

class VaccinationDto{
    private Integer vaccinationId;

    public Integer getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(Integer vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    @Override
    public String toString() {
        return "VaccinationDto{" +
                "vaccinationId=" + vaccinationId +
                '}';
    }
}

@RestController
public class VaccinationRestController {
    @Autowired
    MemberVaccinationRepository vaccinationRepository;

    @PostMapping("/staff/update-vaccination-status")
    public ResponseEntity<String> updateStatus(@RequestBody VaccinationDto dto) {
        System.out.println(dto);
        String vaccinationId = dto.getVaccinationId().toString();
        MemberVaccination vaccination = vaccinationRepository.findById(Long.valueOf(dto.getVaccinationId()))
                .orElse(null);
        if(vaccination!=null){
            if(vaccination.getStatus() == VaccinationStatus.VACCINATED ||
                    vaccination.getStatus() == VaccinationStatus.DISCARDED) {
                String message = "Status already update for " + vaccinationId;
                return new ResponseEntity<String>(message, HttpStatus.ALREADY_REPORTED);
            }
            vaccination.setStatus(VaccinationStatus.VACCINATED);
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
