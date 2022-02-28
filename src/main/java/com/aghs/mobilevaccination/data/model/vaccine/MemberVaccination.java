package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.Spot;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Entity
@Table
public class MemberVaccination {
    @Id
    @Column
    @GeneratedValue
    private String id;
    @Column(length = 6)
    private String pin;
    @ManyToOne
    private VaccineDrive vaccineDrive;
    @ManyToOne
    private Spot vaccinationSpot;
    @ManyToOne
    private Member recipient;
    @Column
    private Date registeredAt;
    @Column
    private Date selectedDate;
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

    public Spot getVaccinationSpot() {
        return vaccinationSpot;
    }

    public void setVaccinationSpot(Spot vaccinationSpot) {
        this.vaccinationSpot = vaccinationSpot;
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

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Date getVaccinatedAt() {
        return vaccinatedAt;
    }

    public void setVaccinatedAt(Date vaccinatedAt) {
        this.vaccinatedAt = vaccinatedAt;
    }

    public void generatePIN(int length) {
        Random random = new Random();
        pin = "";
        for(int i=0; i < length; i++) {
            pin += String.valueOf(random.nextInt(10));
        }
    }
}
