package com.aghs.mobilevaccination.data.dto;

import com.aghs.mobilevaccination.data.model.Disease;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;

public class VaccinationStatusDto {
    private Disease disease;
    private Vaccine vaccine;
    private int count;
    private VaccinationStatus status;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public VaccinationStatus getStatus() {
        return status;
    }

    public void setStatus(VaccinationStatus status) {
        this.status = status;
    }

    // increment the count
    public void incrementCount() {
        count++;
    }
}
