package com.aghs.mobilevaccination.data.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class State {
    @Id
    @Column
    private String name;
}
