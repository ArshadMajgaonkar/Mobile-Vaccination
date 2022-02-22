package com.aghs.mobilevaccination.data.model.location;

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
    @ManyToOne
    private Spot situatedAt;
    @Column
    private Date addedAt;
    @ManyToOne
    private Staff AddedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return AddedBy;
    }

    public void setAddedBy(Staff addedBy) {
        AddedBy = addedBy;
    }
}
