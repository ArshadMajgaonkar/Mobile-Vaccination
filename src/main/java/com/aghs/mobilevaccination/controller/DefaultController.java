package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreSelectDto;
import com.aghs.mobilevaccination.data.model.location.*;
import com.aghs.mobilevaccination.data.repository.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class DefaultController {
    @Autowired
    CentreRepository centreRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    StateRepository stateRepository;

    protected Centre getCentre(CentreSelectDto centreSelectDto, Model model) {
        model.addAttribute("states", stateRepository.findAll());
        if(centreSelectDto.getStateName()!=null && !centreSelectDto.getStateName().equals("")) {
            State selectedState = stateRepository.findByName(centreSelectDto.getStateName());
            model.addAttribute("districts", districtRepository.findByState(selectedState));
            if(centreSelectDto.getDistrictId() != -1) {
                District selectedDistrict =
                        districtRepository.findByIdAndState(centreSelectDto.getDistrictId(), selectedState);
                model.addAttribute("cities", cityRepository.findByDistrict(selectedDistrict));
                if(centreSelectDto.getCityId() != -1) {
                    City selectedCity =
                            cityRepository.findByIdAndDistrict(centreSelectDto.getCityId(), selectedDistrict);
                    List<Spot> spots = spotRepository.findByCity(selectedCity);
                    List<Centre> centres = new ArrayList<>();
                    spots.forEach( spot -> { centres.addAll(centreRepository.findBySpot(spot));});
                    model.addAttribute("centres", centres);
                    if(centreSelectDto.getCentreId() != -1) {
                        return centreRepository.findById(centreSelectDto.getCentreId()).orElse(null);
                    }
                }
            }
        }
        return null;
    }
}
