package com.aghs.mobilevaccination.data.dto;

public class SlotDto {
    private Long driveId;
    private String vaccineName;
    private Long remainingSlot;

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Long getRemainingSlot() {
        return remainingSlot;
    }

    public void setRemainingSlot(Long remainingSlot) {
        this.remainingSlot = remainingSlot;
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }
}
