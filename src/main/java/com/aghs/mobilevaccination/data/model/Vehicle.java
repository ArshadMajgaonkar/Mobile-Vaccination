package com.aghs.mobilevaccination.data.model;

import com.aghs.mobilevaccination.data.model.location.Centre;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Vehicle {
    @Id
    @Column
    private String registrationNumber;
    // working at
    @ManyToOne
    private Centre centre;
    @Column
    private Date addedAt;
    @ManyToOne
    private Staff addedBy;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Staff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Staff addedBy) {
        this.addedBy = addedBy;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", centre=" + centre +
                ", addedAt=" + addedAt +
                ", addedBy=" + addedBy +
                '}';
    }
}
