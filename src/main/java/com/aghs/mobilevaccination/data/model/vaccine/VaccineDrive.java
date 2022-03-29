package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.dto.DriveDto;
import com.aghs.mobilevaccination.data.dto.SlotDto;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.model.Vehicle;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.repository.StaffRepository;
import com.aghs.mobilevaccination.data.repository.VehicleRepository;
import com.aghs.mobilevaccination.data.repository.location.CityRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

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

    public long getRegistration(Map<String, Long> spotWiseRegistrations,
                                 SpotRepository spotRepository,
                                 MemberVaccinationRepository vaccinationRepository) {
        List<Spot> spots = spotRepository.findByCity(city);
        long registrationCount = 0;
        for(Spot spot: spots) {
            List<MemberVaccination> memberVaccinations =
                    vaccinationRepository.findByVaccineDriveAndVaccinationSpotAndStatus(
                            this, spot, VaccinationStatus.REGISTERED);
            String location = spot.getWard() + " : " + spot.getLocalityNames();
            spotWiseRegistrations.put(location, (long) memberVaccinations.size());
            registrationCount += memberVaccinations.size();
        }
        return registrationCount;
    }

    public long getRegistrationCountWithSpotId(Map<Long, Long> spotWiseRegistrations,
                                               SpotRepository spotRepository,
                                               MemberVaccinationRepository vaccinationRepository) {
        List<Spot> spots = spotRepository.findByCity(this.getCity());
        long registrationCount = 0;
        for(Spot spot: spots) {
            List<MemberVaccination> memberVaccinations =
                    vaccinationRepository.findByVaccineDriveAndVaccinationSpotAndStatus(
                            this, spot, VaccinationStatus.REGISTERED);
            spotWiseRegistrations.put(spot.getId(), (long) memberVaccinations.size());
            registrationCount += memberVaccinations.size();
        }
        return registrationCount;
    }

    public Long getRemainingSlot(MemberVaccinationRepository vaccinationRepository) {
        Integer bookedCount = vaccinationRepository.findByVaccineDriveAndStatus(this, VaccinationStatus.REGISTERED)
                .size();
        // TODO: think to add about vaccinated as booked. It's illogical to happen practically.
        return this.getSlotCount() - bookedCount;
    }

    public static boolean isDuplicate(LocalDate driveDate,
                                      Long cityId,
                                      String vaccineName,
                                      CityRepository cityRepository,
                                      VaccineRepository vaccineRepository,
                                      VaccineDriveRepository driveRepository) {
        if(cityId!=null && vaccineName!=null) {
            City city = cityRepository.findById(cityId).orElse(null);
            Vaccine vaccine = vaccineRepository.findById(vaccineName).orElse(null);
            if (city != null && vaccine != null) {
                return driveRepository.findByDriveDateAndStatusAndCityAndVaccine(
                        driveDate,
                        VaccineDriveStatus.BOOKING,
                        city,
                        vaccine
                )!=null;
            }
        }
        return false;
    }

    private void moveToInCentre(Set<Long> spots,
                                SpotRepository spotRepository,
                                MemberVaccinationRepository vaccinationRepository) {
        for(Long spotId: spots) {
            Spot spot = spotRepository.findById(spotId).orElse(null);
            if(spot!=null) {
                List<MemberVaccination> vaccinations =
                        vaccinationRepository.findByVaccineDriveAndVaccinationSpotAndStatus(
                                this, spot, VaccinationStatus.REGISTERED
                        );
                for(MemberVaccination vaccination : vaccinations) {
                    vaccination.setStatus(VaccinationStatus.IN_CENTRE);
                    vaccinationRepository.save(vaccination);
                }
            }
        }
    }

    public void removeAllocatedVehicleAndStaff(List<Staff> staffList,
                                               List<Vehicle> vehicles,
                                               VaccineDriveRepository driveRepository) {
        List<VaccineDriveStatus> validStatus =
                Arrays.asList(VaccineDriveStatus.UPCOMING, VaccineDriveStatus.ON_GOING, VaccineDriveStatus.COMPLETED);
        List<VaccineDrive> drives =
                driveRepository.findByDriveDateAndCityAndStatusIn(this.getDriveDate(), this.getCity(), validStatus);
        for(VaccineDrive drive: drives) {
            staffList.remove(drive.getVaccinator());
            vehicles.remove(drive.getVehicle());
        }
    }

    /**
     * Saves VaccineDrive as well as updates respective MemberVaccination
     * @param spotDistribution List of Spot distributed in list based on different drives
     * @param driveDate VaccineDrive conduction date
     * @param city The city in which VaccineDrive will be conducted.
     * @param dosesPerVehicle Vaccine Doses Count for vehicle.
     */
    public void saveVaccineDrives(List<List<?>> spotDistribution,
                                  int dosesPerVehicle,
                                  Set<Long> allVaccinationSpot,
                                  MemberVaccinationRepository vaccinationRepository,
                                  SpotRepository spotRepository,
                                  StaffRepository staffRepository,
                                  VaccineDriveRepository driveRepository) {
        // Add first spotList to this drive
        int firstElement = 0;
        List<Spot> firstSpotList = Spot.getSpots(spotDistribution.get(firstElement), spotRepository);
        this.setStatus(VaccineDriveStatus.INADEQUATE_DATA);
        this.setSlotCount((long)dosesPerVehicle);
        this.setVaccinationSpots(new HashSet<>(firstSpotList));
        driveRepository.save(this);
        spotDistribution.remove(firstElement);
        // removing spot to trace unselected spots
        firstSpotList.forEach( spot -> { allVaccinationSpot.remove(spot.getId()); } );
        // Add other drives
        for(List<?> spotIdList: spotDistribution) {
            List<Spot> spots = Spot.getSpots(spotIdList, spotRepository);
            VaccineDrive vaccineDrive = new VaccineDrive();
            vaccineDrive.setDriveDate(this.getDriveDate());
            vaccineDrive.setVaccine(this.getVaccine());
            vaccineDrive.setStatus(VaccineDriveStatus.INADEQUATE_DATA);
            vaccineDrive.setCity(this.getCity());
            vaccineDrive.setVaccinationSpots(new HashSet<>(spots));
            vaccineDrive.setSlotCount((long)dosesPerVehicle);
            vaccineDrive.setAddedAt(new Date());
            // TODO: update AddedBy using session user
            vaccineDrive.setAddedBy(staffRepository.findByUsername("admin"));
            driveRepository.save(vaccineDrive);
            // updating vaccineDrive
            vaccineDrive.updateMemberVaccinations(this, vaccinationRepository);
            // removing spot to trace unselected spots
            spots.forEach( spot -> { allVaccinationSpot.remove(spot.getId()); } );
        }
        // processing vaccination of unselected spots
        this.moveToInCentre(allVaccinationSpot, spotRepository, vaccinationRepository);
    }

    public void updateFromDto(DriveDto driveDto,
                              StaffRepository staffRepository,
                              VehicleRepository vehicleRepository) {
        this.setVehicle(vehicleRepository.findByRegistrationNumber(driveDto.getVehicleRegNo()));
        this.setVaccinator(staffRepository.findByUsername(driveDto.getVaccinatorUsername()));
    }

    public void updateMemberVaccinations(VaccineDrive parentDrive, MemberVaccinationRepository memberVaccinationRepository) {
        // TODO : update vaccination of unselected Spot
        for(Spot spot: this.getVaccinationSpots()) {
            List<MemberVaccination> vaccinations = memberVaccinationRepository.findByVaccinationSpotAndVaccineDrive(
                    spot, parentDrive);
            System.out.println(vaccinations);
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
