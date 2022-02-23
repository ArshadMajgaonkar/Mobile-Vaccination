package com.aghs.mobilevaccination.data.model;

import com.aghs.mobilevaccination.data.model.location.Centre;

import javax.persistence.*;
import java.util.Date;

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
}
