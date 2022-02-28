package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class VaccineCategory {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @ManyToOne(optional = false)
    private Vaccine vaccine;
    @Column
    private int minAgeLimit;
    @Column
    private int maxAgeLimit;
    @ManyToOne
    @JoinColumn
    private VaccineCategory prerequisite;
    @Column(nullable = false)
    private Date addedAt;
    @ManyToOne(optional = false)
    private Staff addedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(int minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public int getMaxAgeLimit() {
        return maxAgeLimit;
    }

    public void setMaxAgeLimit(int maxAgeLimit) {
        this.maxAgeLimit = maxAgeLimit;
    }

    public VaccineCategory getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(VaccineCategory prerequisite) {
        this.prerequisite = prerequisite;
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
