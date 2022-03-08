package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.location.CentreRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Spot spot;
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

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
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


    public static List<Centre> getCentresBySpots(List<Spot> spots, CentreRepository centreRepository) {
        List<Centre> centres = new ArrayList<>();
        for(Spot spot: spots) {
            centres.addAll(centreRepository.findBySpot(spot));
        }
        return centres;
    }


    // Conversions

    public static Centre fromDto(String name, Spot spot, Staff staff) {
        Centre centre = new Centre();
        centre.setName(name);
        centre.setSpot(spot);
        centre.setAddedAt(new Date());
        centre.setAddedBy(staff);
        return centre;
    }
}
