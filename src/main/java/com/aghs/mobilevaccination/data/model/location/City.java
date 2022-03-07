package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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
    @Column
    private Long allotedSlotsPerDay;

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

    public Long getAllotedSlotsPerDay() {
        return allotedSlotsPerDay;
    }

    public void setAllotedSlotsPerDay(Long allotedSlotsPerDay) {
        this.allotedSlotsPerDay = allotedSlotsPerDay;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", district=" + district +
                ", allotedSlotsPerDay=" + allotedSlotsPerDay +
                '}';
    }

    public static long getRemainingSlots(MemberVaccinationRepository memberVaccinationRepository,
                                         LocalDate selectedDate,
                                         List<Spot> spots) {
        AtomicLong bookedSlot = new AtomicLong(0L);
        spots.forEach(spot -> {
            bookedSlot.addAndGet(
                    memberVaccinationRepository.findByVaccinationSpotAndSelectedDate(spot, selectedDate).size()
            );
        });
        return bookedSlot.get();
    }

    public CityDto toDto() {
        CityDto dto = new CityDto();
        dto.setCityId(this.id);
        dto.setDistrictId(this.district.getId());
        dto.setStateName(this.district.getState().getName());
        return dto;
    }

}
