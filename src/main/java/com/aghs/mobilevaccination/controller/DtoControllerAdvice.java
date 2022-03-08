package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreDto;
import com.aghs.mobilevaccination.data.dto.CentreSelectDto;
import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class DtoControllerAdvice {

    @ModelAttribute("centreDto")
    public CentreDto getDefaultCentreDto() {
        return new CentreDto(null, null, -1, -1, -1);
    }

    @ModelAttribute("centreSelectDto")
    public CentreSelectDto getCentreSelectDto() {
        return new CentreSelectDto(null, -1, -1, -1);
    }

    @ModelAttribute("cityDto")
    public CityDto getCityDto() {
        return new CityDto();
    }

    @ModelAttribute("vaccineDrive")
    public VaccineDrive getVaccineDrive() {
        return new VaccineDrive();
    }

    @ModelAttribute("spotDto")
    public SpotDto getSpotDTO() {
        return new SpotDto();
    }
}
