package com.aghs.mobilevaccination.data.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Vaccine Recipient which will be linked with Account object.
 * Relation:     Member(many) <----> Account(one)
 */
@Entity
@Table
public class Member {

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    private Date addedAt;

}
