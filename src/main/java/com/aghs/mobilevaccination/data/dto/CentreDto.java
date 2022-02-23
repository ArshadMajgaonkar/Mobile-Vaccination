package com.aghs.mobilevaccination.data.dto;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.location.State;

public class CentreDto {
    private String name;
    private String stateName;
    private long districtId;
    private long cityId;
    private long spotId;

    public CentreDto(String name, String stateName, long districtId, long cityId, long spotId) {
        this.name = name;
        this.stateName = stateName;
        this.districtId = districtId;
        this.cityId = cityId;
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String state) {
        this.stateName = state;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getSpotId() {
        return spotId;
    }

    public void setSpotId(long spotId) {
        this.spotId = spotId;
    }

    @Override
    public String toString() {
        return "CentreDto{" +
                "name='" + name + '\'' +
                ", state=" + stateName +
                ", district=" + districtId +
                ", city=" + cityId +
                ", spot=" + spotId +
                '}';
    }

    // Methods

    public boolean isValid() {
        return (name!=null && !name.equals("") &&
                stateName != null && !stateName.equals("") &&
                districtId != -1 && spotId != -1);
    }
}
