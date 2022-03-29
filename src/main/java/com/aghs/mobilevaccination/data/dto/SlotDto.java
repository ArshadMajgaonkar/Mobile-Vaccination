package com.aghs.mobilevaccination.data.dto;

import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineCategory;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDrive;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineDriveStatus;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineDriveRepository;

import java.awt.image.ShortLookupTable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SlotDto {
    private Long driveId;
    private String vaccineName;
    private Long remainingSlot;
    private VaccineCategory vaccineCategory;

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Long getRemainingSlot() {
        return remainingSlot;
    }

    public void setRemainingSlot(Long remainingSlot) {
        this.remainingSlot = remainingSlot;
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public VaccineCategory getVaccineCategory() {
        return vaccineCategory;
    }

    public void setVaccineCategory(VaccineCategory vaccineCategory) {
        this.vaccineCategory = vaccineCategory;
    }


    // Static Methods

    public static List<SlotDto> fromCategory(LocalDate driveDate,
                                             City city,
                                             List<VaccineCategory> vaccineCategories,
                                             VaccineDriveRepository driveRepository,
                                             MemberVaccinationRepository vaccinationRepository) {
        List<SlotDto> dtoList = new ArrayList<>();
        for(VaccineCategory category: vaccineCategories) {
            VaccineDrive drive = driveRepository.findByDriveDateAndStatusAndCityAndVaccine(
                    driveDate,
                    VaccineDriveStatus.BOOKING,
                    city,
                    category.getVaccine()
            );
            if(drive==null)
                continue;
            long remainingSlot = drive.getRemainingSlot(vaccinationRepository);
            SlotDto dto = new SlotDto();
            dto.setDriveId(drive.getId());
            dto.setRemainingSlot(remainingSlot);
            dto.setVaccineCategory(category);
            dtoList.add(dto);
        }
        return dtoList;
    }

}
