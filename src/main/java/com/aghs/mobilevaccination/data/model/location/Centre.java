package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.dto.CentreDto;
import com.aghs.mobilevaccination.data.model.Staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Centre {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 100)
    private String name;
    @ManyToOne
    private Spot situatedAt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Spot getSituatedAt() {
        return situatedAt;
    }

    public void setSituatedAt(Spot situatedAt) {
        this.situatedAt = situatedAt;
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

    // Static Methods

    public static Centre fromDto(String name, Spot spot, Staff staff) {
        Centre centre = new Centre();
        centre.setName(name);
        centre.setSituatedAt(spot);
        centre.setAddedAt(new Date());
        centre.setAddedBy(staff);
        return centre;
    }
}
