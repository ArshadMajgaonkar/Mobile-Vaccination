package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class VaccineCategory {
    @Id
    @Column
    private String name;
    @Column
    private int minAgeLimit;
    @Column
    private int maxAgeLimit;
    @Column
    private int maxDoseCount;
    @Column
    private Date addedAt;
    @ManyToOne
    private Staff addedBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getMaxDoseCount() {
        return maxDoseCount;
    }

    public void setMaxDoseCount(int maxDoseCount) {
        this.maxDoseCount = maxDoseCount;
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
