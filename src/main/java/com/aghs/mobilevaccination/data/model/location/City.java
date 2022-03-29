package com.aghs.mobilevaccination.data.model.location;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.model.AuthGroup;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import com.aghs.mobilevaccination.data.repository.location.CentreRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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


    // Static Methods

    public List<Staff> getVaccinators(CentreRepository centreRepository,
                                      SpotRepository spotRepository,
                                      StaffRepository staffRepository) {
        List<Spot> spots = spotRepository.findByCity(this);
        List<Centre> centres = centreRepository.findBySpotIn(spots);
        return staffRepository.findByRoleAndCentreIn(AuthGroup.VACCINATOR, centres);
    }

    public List<Vehicle> getVehicle(SpotRepository spotRepository,
                                    CentreRepository centreRepository,
                                    VehicleRepository vehicleRepository) {
        // Listing all Vehicle in the City, spot by spot
        List<Vehicle> vehiclesInCity = new ArrayList<>();
        List<Spot> spotsInCity = spotRepository.findByCity(this);
        for(Spot spot: spotsInCity) {
            List<Centre> centresInCity = centreRepository.findBySpot(spot);
            for (Centre centre : centresInCity) {
                vehiclesInCity.addAll(vehicleRepository.findByCentre(centre));
            }
        }
        return vehiclesInCity;
    }


    // Conversions

    public CityDto toDto() {
        CityDto dto = new CityDto();
        dto.setCityId(this.id);
        dto.setDistrictId(this.district.getId());
        dto.setStateName(this.district.getState().getName());
        return dto;
    }

}
