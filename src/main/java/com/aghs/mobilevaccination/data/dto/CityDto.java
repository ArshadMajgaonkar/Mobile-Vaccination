package com.aghs.mobilevaccination.data.dto;

public class CityDto {
    private String stateName;
    private Long districtId;
    private Long cityId;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "stateName='" + stateName + '\'' +
                ", districtId=" + districtId +
                ", cityId=" + cityId +
                '}';
    }
}
