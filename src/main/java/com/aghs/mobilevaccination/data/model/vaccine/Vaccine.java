package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Vaccine {
    @Id
    @Column
    private String name;
    @Column(unique = true, length = 3)
    private String code;
    // To take another vaccine
    @Column
    private Long intervalInDays;
    @ManyToOne
    private Staff addedBy;
    @Column
    private Date addedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getIntervalInDays() {
        return intervalInDays;
    }

    public void setIntervalInDays(Long intervalInDays) {
        this.intervalInDays = intervalInDays;
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
