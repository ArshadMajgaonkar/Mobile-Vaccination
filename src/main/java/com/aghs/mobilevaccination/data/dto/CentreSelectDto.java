package com.aghs.mobilevaccination.data.dto;

public class CentreSelectDto {
    private String stateName;
    private long districtId;
    private long cityId;
    private long centreId;

    public CentreSelectDto() {
    }

    public CentreSelectDto(String stateName, long districtId, long cityId, long centreId) {
        this.stateName = stateName;
        this.districtId = districtId;
        this.cityId = cityId;
        this.centreId = centreId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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

    public long getCentreId() {
        return centreId;
    }

    public void setCentreId(long centreId) {
        this.centreId = centreId;
    }

    @Override
    public String toString() {
        return "CentreSelectDto{" +
                "stateName='" + stateName + '\'' +
                ", districtId=" + districtId +
                ", cityId=" + cityId +
                ", centreId=" + centreId +
                '}';
    }
}
