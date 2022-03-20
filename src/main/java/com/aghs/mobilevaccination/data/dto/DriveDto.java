package com.aghs.mobilevaccination.data.dto;

import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;

public class DriveDto {
    Long driveId;
    VaccineDriveStatus status;
    String vehicleRegNo;
    String vaccineName;
    String vaccinatorUsername;

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public VaccineDriveStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineDriveStatus status) {
        this.status = status;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getVaccinatorUsername() {
        return vaccinatorUsername;
    }

    public void setVaccinatorUsername(String vaccinatorUsername) {
        this.vaccinatorUsername = vaccinatorUsername;
    }

    @Override
    public String toString() {
        return "DriveDto{" +
                "driveId=" + driveId +
                ", vehicleRegNo='" + vehicleRegNo + '\'' +
                ", vaccineName='" + vaccineName + '\'' +
                ", vaccinatorUsername='" + vaccinatorUsername + '\'' +
                '}';
    }

    public boolean isValid() {
        return driveId != null && vehicleRegNo != null && vaccineName != null && vaccinatorUsername != null;
    }
}
