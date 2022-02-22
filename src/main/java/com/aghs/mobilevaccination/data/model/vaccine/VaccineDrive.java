package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.Spot;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table
public class VaccineDrive {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private Date driveDate;
    @ManyToOne
    private VaccineCategory vaccineCategory;
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private Staff vaccinator;
    @Column
    @Enumerated(value = EnumType.STRING)
    private VaccineDriveStatus status;
    @ManyToMany
    private Set<Spot> vaccinationSpots;
    @ManyToOne
    private Staff addedBy;
    @Column
    private Date addedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(Date driveDate) {
        this.driveDate = driveDate;
    }

    public VaccineCategory getVaccineCategory() {
        return vaccineCategory;
    }

    public void setVaccineCategory(VaccineCategory vaccineCategory) {
        this.vaccineCategory = vaccineCategory;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Staff getVaccinator() {
        return vaccinator;
    }

    public void setVaccinator(Staff vaccinator) {
        this.vaccinator = vaccinator;
    }

    public VaccineDriveStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineDriveStatus status) {
        this.status = status;
    }

    public Set<Spot> getVaccinationSpots() {
        return vaccinationSpots;
    }

    public void setVaccinationSpots(Set<Spot> vaccinationSpots) {
        this.vaccinationSpots = vaccinationSpots;
    }

    public Staff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Staff addedBy) {
        this.addedBy = addedBy;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
}
