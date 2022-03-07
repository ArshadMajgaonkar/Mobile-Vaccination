package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CentreSelectDto;
import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.location.*;
import com.aghs.mobilevaccination.data.repository.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    public Spot getSpotWithSelectedCityAndItsSpots(SpotDto spotDto, Model model, City city, List<Spot> spots) {
        model.addAttribute("states", stateRepository.findAll());
        if(spotDto.getStateName() != null) {
            State selectedState = stateRepository.findByName(spotDto.getStateName());
            model.addAttribute("districts", districtRepository.findByState(selectedState));
            if(spotDto.getDistrictId() != null &&
                    districtRepository.findByIdAndState(spotDto.getDistrictId(), selectedState) != null) {
                District selectedDistrict = districtRepository.findById(spotDto.getDistrictId()).orElse(null);
                model.addAttribute("cities", cityRepository.findByDistrict(selectedDistrict));
                if(spotDto.getCityId() != null &&
                        cityRepository.findByIdAndDistrict(spotDto.getCityId(), selectedDistrict)!=null) {
                    City selectedCity = cityRepository.findById(spotDto.getCityId()).orElse(null);
                    city.setAllotedSlotsPerDay(selectedCity.getAllotedSlotsPerDay());
                    spots.addAll(spotRepository.findByCity(selectedCity));
                    model.addAttribute("spots", spots);
                    if(spotDto.getSpotId() != null &&
                            spotRepository.findByIdAndCity(spotDto.getSpotId(), selectedCity)!=null) {
                        return spotRepository.findById(spotDto.getSpotId()).orElse(null);
                    }
                }
            }
        }
        return null;
    }

    public Spot getSpot(SpotDto spotDto, Model model) {
        City city = new City();
        List<Spot> spots = new ArrayList<>();
        return getSpotWithSelectedCityAndItsSpots(spotDto, model, city, spots);
    }

    public City getCity(CityDto cityDto, Model model) {
        model.addAttribute("states", stateRepository.findAll());
        if(cityDto.getStateName() != null) {
            State selectedState = stateRepository.findByName(cityDto.getStateName());
            model.addAttribute("districts", districtRepository.findByState(selectedState));
            if(cityDto.getDistrictId() != null &&
                    districtRepository.findByIdAndState(cityDto.getDistrictId(), selectedState) != null) {
                District selectedDistrict = districtRepository.findById(cityDto.getDistrictId()).orElse(null);
                model.addAttribute("cities", cityRepository.findByDistrict(selectedDistrict));
                if(cityDto.getCityId() != null &&
                        cityRepository.findByIdAndDistrict(cityDto.getCityId(), selectedDistrict)!=null) {
                    return cityRepository.findById(cityDto.getCityId()).orElse(null);
                }
            }
        }
        return null;
    }
}
