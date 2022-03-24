package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.dto.DriveDto;
import com.aghs.mobilevaccination.data.dto.SlotDto;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class VaccineDrive {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private LocalDate driveDate;
    @ManyToOne
    private Vaccine vaccine;
    @ManyToOne
    private Vehicle vehicle;
    @Column(nullable = false)
    private Long slotCount;
    @ManyToOne
    private Staff vaccinator;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private VaccineDriveStatus status;
    @ManyToOne
    private City city;
    @ManyToMany
    private Set<Spot> vaccinationSpots;
    @ManyToOne/*(optional = false)*/
    private Staff addedBy;
    @Column(nullable = false)
    private Date addedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Long getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(Long slotCount) {
        this.slotCount = slotCount;
    }

    public Staff getVaccinator() {
        return vaccinator;
    }

    public void setVaccinator(Staff vaccinator) {
        this.vaccinator = vaccinator;
    }

    public VaccineDriveStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineDriveStatus status) {
        this.status = status;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Spot> getVaccinationSpots() {
        return vaccinationSpots;
    }

    public void setVaccinationSpots(Set<Spot> vaccinationSpots) {
        this.vaccinationSpots = vaccinationSpots;
    }

    public Staff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Staff addedBy) {
        this.addedBy = addedBy;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }


    // Validation

    public boolean isUpdateValid() {
        if(this.id != null &&
                this.addedAt != null &&
                this.addedBy != null &&
                this.city != null  &&
                this.driveDate != null &&
                this.slotCount != null &&
                this.status!=null &&
                this.vaccinationSpots != null &&
                this.vaccinator != null &&
                this.vaccine != null &&
                this.vehicle != null)
            return true;
        return false;
    }


    // Method

    public Long getRemainingSlot(MemberVaccinationRepository vaccinationRepository) {
        Integer bookedCount = vaccinationRepository.findByVaccineDriveAndStatus(this, VaccinationStatus.REGISTERED)
                .size();
        // TODO: think to add about vaccinated as booked. It's illogical to happen practically.
        return this.getSlotCount() - bookedCount;
    }

    public void updateFromDto(DriveDto driveDto,
                              StaffRepository staffRepository,
                              VehicleRepository vehicleRepository,
                              VaccineRepository vaccineRepository) {
        this.setVehicle(vehicleRepository.findByRegistrationNumber(driveDto.getVehicleRegNo()));
        this.setVaccine(vaccineRepository.findById(driveDto.getVaccineName()).orElse(null));
        this.setVaccinator(staffRepository.findByUsername(driveDto.getVaccinatorUsername()));
    }

    public void updateMemberVaccinations(MemberVaccinationRepository memberVaccinationRepository) {
        for(Spot spot: this.getVaccinationSpots()) {
            List<MemberVaccination> vaccinations = memberVaccinationRepository.findByVaccinationSpotAndSelectedDate(
                    spot, this.getDriveDate());
            for (MemberVaccination vaccination : vaccinations) {
                vaccination.setVaccineDrive(this);
                memberVaccinationRepository.save(vaccination);
            }
        }
    }


    // Static Methods

    public static List<SlotDto> toDto(MemberVaccinationRepository vaccinationRepository,List<VaccineDrive> drives) {
        List<SlotDto> dtoList = new ArrayList<>();
        for(VaccineDrive drive: drives) {
            Long remainingSlot = drive.getRemainingSlot(vaccinationRepository);
            SlotDto dto = new SlotDto();
            dto.setVaccineName(drive.getVaccine().getName());
            dto.setRemainingSlot(remainingSlot);
            dto.setDriveId(drive.id);
            dtoList.add(dto);
        }
        return dtoList;
    }

}
