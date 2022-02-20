package com.aghs.mobilevaccination.data.model.location;

import javax.persistence.*;

@Entity
@Table
public class City {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column
    private String name;
    @ManyToOne
    private District district;

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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", district=" + district +
                '}';
    }

}
