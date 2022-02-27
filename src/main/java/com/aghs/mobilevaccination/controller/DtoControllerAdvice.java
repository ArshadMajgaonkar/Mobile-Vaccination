package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreDto;
import com.aghs.mobilevaccination.data.dto.CentreSelectDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
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

    @ModelAttribute("spotDto")
    public SpotDto getSpotDTO() {
        return new SpotDto();
    }
}
