package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

public class MemberVaccination {
    @Id
    @Column
    @GeneratedValue
    private String id;
    @Column(length = 6)
    private String pin;
    @Column
    private VaccineDrive vaccineDrive;
    @Column
    private Spot vaccinateAt;
    @ManyToOne
    private Member recipient;
    @Column
    private Date registeredAt;
    @Column
    private Date vaccinatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public VaccineDrive getVaccineDrive() {
        return vaccineDrive;
    }

    public void setVaccineDrive(VaccineDrive vaccineDrive) {
        this.vaccineDrive = vaccineDrive;
    }

    public Spot getVaccinateAt() {
        return vaccinateAt;
    }

    public void setVaccinateAt(Spot vaccinateAt) {
        this.vaccinateAt = vaccinateAt;
    }

    public Member getRecipient() {
        return recipient;
    }

    public void setRecipient(Member recipient) {
        this.recipient = recipient;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Date getVaccinatedAt() {
        return vaccinatedAt;
    }

    public void setVaccinatedAt(Date vaccinatedAt) {
        this.vaccinatedAt = vaccinatedAt;
    }
}
