package com.aghs.mobilevaccination.data.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Vehicle {
    @Id
    @Column
    private String registrationNumber;
    @Column
    private Date addedAt;
    @ManyToOne
    private Staff addedBy;
}
