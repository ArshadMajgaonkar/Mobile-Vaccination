package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;
    @Column
    private String ward;
    @Column
    private String pinCode;
    @OneToOne
    private City city;
    @Column
    private Date addedAt;
    @ManyToOne
    private Staff addedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
        return "Location{" +
                "id=" + id +
                ", ward='" + ward + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", city=" + city +
                ", addedAt=" + addedAt +
                ", addedBy=" + addedBy +
                '}';
    }
}
