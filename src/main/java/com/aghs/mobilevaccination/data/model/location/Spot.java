package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private long id;
    @Column(nullable = false)
    private String ward;
    // separated with comma
    @Column(nullable = false)
    private String localityNames;
    @Column(nullable = false)
    private String pinCode;
    @ManyToOne(optional = false)
    private City city;
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

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getLocalityNames() {
        return localityNames;
    }

    public void setLocalityNames(String localityNames) {
        this.localityNames = localityNames;
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
