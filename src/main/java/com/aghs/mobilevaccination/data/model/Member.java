package com.aghs.mobilevaccination.data.model;

import com.aghs.mobilevaccination.data.dto.VaccinationStatusDto;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.model.vaccine.VaccineCategory;
import com.aghs.mobilevaccination.data.repository.DiseaseRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineCategoryRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.google.common.hash.Hashing;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Vaccine Recipient which will be linked with Account object.
 * Relation:     Member(many) <----> Account(one)
 */
@Entity
@Table
public class Member {

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;
    @Column(nullable = false, unique = true, length = 511)
    private String aadharId;
    @Column(nullable = false)
    private String aadharLast4Digit;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String birthYear;
    @Column(nullable = false)
    private Date addedAt;
    @ManyToOne
    private Account linkedAccount;

    // Constants
    private static final int LOWER_AGE_LIMIT = 18;
    private static final int UPPER_AGE_LIMIT = 110;

    // Getters and Setters

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAadharId() {
        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }

    public String getAadharLast4Digit() {
        return aadharLast4Digit;
    }

    public void setAadharLast4Digit(String aadharLast4Digit) {
        this.aadharLast4Digit = aadharLast4Digit;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public Account getLinkedAccount() {
        return linkedAccount;
    }

    public void setLinkedAccount(Account linkedAccount) {
        this.linkedAccount = linkedAccount;
    }

    @Override
    public String toString() {
        return "Member{" +
                "userId=" + userId +
                ", aadharId='" + aadharId + '\'' +
                ", aadharLast4Digit='" + aadharLast4Digit + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", addedAt=" + addedAt +
                ", linkedAccount=" + linkedAccount +
                '}';
    }

    public void processData() {
        aadharLast4Digit = aadharId.substring(8, 12);
        aadharId = Hashing.sha512().hashString(aadharId, StandardCharsets.UTF_8).toString();
        addedAt = new Date();
    }


    // Validations

    public boolean isAadharIdValid() {
        return Pattern.matches("^[0-9]{12}$", aadharId);
    }

    public boolean isFullNameValid() {
        return Pattern.matches("^[A-Za-z\\s]+$", fullName) && !fullName.isBlank();
    }

    public boolean isBirthYearValid() {
        return Pattern.matches("^[0-9]{4}$", this.birthYear)
                && LocalDate.now().minusYears(LOWER_AGE_LIMIT).getYear() > Integer.parseInt(birthYear)
                && LocalDate.now().minusYears(UPPER_AGE_LIMIT).getYear() < Integer.parseInt(birthYear);
    }


    // Methods

    public void discardUnvaccinatedRegistration(MemberVaccinationRepository vaccinationRepository) {
        List<MemberVaccination> registrations =
                vaccinationRepository.findByRecipientAndStatus(this, VaccinationStatus.REGISTERED);
        for(MemberVaccination registration: registrations) {
            registration.setStatus(VaccinationStatus.DISCARDED);            // auto-saves the state
        }
    }

    public int getAge() {
        return LocalDate.now().getYear() - Integer.parseInt(this.getBirthYear());
    }

    public List<VaccineCategory> getEligibleVaccineCategory(DiseaseRepository diseaseRepository,
                                                            MemberVaccinationRepository vaccinationRepository,
                                                            VaccineRepository vaccineRepository,
                                                            VaccineCategoryRepository categoryRepository) {
        List<Vaccine> eligibleVaccines = new ArrayList<>();
        List<Disease> vaccinatedDiseases = new ArrayList<>();
        List<VaccineCategory> possibleVaccinationCategory = new ArrayList<>();
        List<MemberVaccination> vaccinations = vaccinationRepository.findByRecipientAndStatus(
                this, VaccinationStatus.VACCINATED);
        for(MemberVaccination vaccination: vaccinations) {
            Vaccine vaccine = vaccination.getVaccineDrive().getVaccine();
            if(!eligibleVaccines.contains(vaccine)) {
                eligibleVaccines.add(vaccine);
                vaccinatedDiseases.add(vaccination.getVaccineDrive().getVaccine().getOfDisease());
                possibleVaccinationCategory.addAll(VaccineCategory.getNextPossibleCategory(
                        this, vaccine, vaccinations, categoryRepository));
            }
        }
        // TODO: try showing possible vaccine category instead of vaccine
        List<Disease> diseases = diseaseRepository.findAll();
        for(Disease disease: diseases) {
            if(!vaccinatedDiseases.contains(disease)) {
                List<Vaccine> vaccines = vaccineRepository.findByOfDisease(disease);
                for(Vaccine vaccine: vaccines) {
                    eligibleVaccines.add(vaccine);
                    possibleVaccinationCategory.addAll(categoryRepository.findByVaccineAndPrerequisite(vaccine, null));
                }
            }
        }
        return possibleVaccinationCategory;
    }

    public boolean hasCompletedVaccinationInterval(MemberVaccinationRepository vaccinationRepository) {
        List<MemberVaccination> memberVaccinations = vaccinationRepository.findByRecipientAndStatusOrderByVaccinatedAt(
                this, VaccinationStatus.VACCINATED);
        System.out.println(memberVaccinations);
        if(memberVaccinations.size() > 0) {
                MemberVaccination vaccination = memberVaccinations.get(memberVaccinations.size()-1);
                if(!vaccination.hasCompletedVaccinationInterval())
                    return false;
            return true;
        }
        return true;
    }

    public VaccineCategory getLatestVaccinatedCategory(Vaccine vaccine,
                                                       List<MemberVaccination> vaccinations) {
        MemberVaccination latestVaccinationOfVaccine = null;
        // uniquely store disease, vaccine and category
        for(MemberVaccination vaccination: vaccinations) {
            Vaccine vaccinatedVaccine = vaccination.getVaccineCategory().getVaccine();
            VaccineCategory category = vaccination.getVaccineCategory();
            if (vaccinatedVaccine == vaccine && (latestVaccinationOfVaccine==null ||
                    latestVaccinationOfVaccine.getVaccinatedAt().after(vaccination.getVaccinatedAt()) ))
                latestVaccinationOfVaccine = vaccination;
        }
        return latestVaccinationOfVaccine.getVaccineCategory();
    }

    public VaccineCategory getLatestVaccinatedCategory(Vaccine vaccine,
                                                             MemberVaccinationRepository vaccinationRepository) {
        MemberVaccination latestVaccinationOfVaccine = null;
        List<MemberVaccination> vaccinations = vaccinationRepository.findByRecipientAndStatus(
                this, VaccinationStatus.VACCINATED);
        // uniquely store disease, vaccine and category
        for(MemberVaccination vaccination: vaccinations) {
            Vaccine vaccinatedVaccine = vaccination.getVaccineCategory().getVaccine();;
            VaccineCategory category = vaccination.getVaccineCategory();
            if (vaccinatedVaccine == vaccine && (latestVaccinationOfVaccine==null ||
                            latestVaccinationOfVaccine.getVaccinatedAt().after(vaccination.getVaccinatedAt()) ))
                latestVaccinationOfVaccine = vaccination;
        }
        return latestVaccinationOfVaccine.getVaccineCategory();
    }

    /**
     * Returns Vaccination Details
     * @param vaccinationRepository To get Vaccination of Member
     * @return List of Vaccinated Disease List, Vaccinated Vaccine List and Vaccinated VaccineCategory List
     */
    public List<List> getVaccinationDetails(MemberVaccinationRepository vaccinationRepository) {
        List<Disease> vaccinatedDiseases = new ArrayList<>();
        List<Vaccine> vaccinatedVaccine = new ArrayList<>();
        List<VaccineCategory> vaccinatedCategory = new ArrayList<>();
        List<MemberVaccination> vaccinations = vaccinationRepository.findByRecipientAndStatus(
                this, VaccinationStatus.VACCINATED);
        // uniquely store disease, vaccine and category
        for(MemberVaccination vaccination: vaccinations) {
            Disease disease = vaccination.getVaccineCategory().getVaccine().getOfDisease();
            Vaccine vaccine = vaccination.getVaccineCategory().getVaccine();
            VaccineCategory category = vaccination.getVaccineCategory();
            if( !vaccinatedDiseases.contains(disease))
                vaccinatedDiseases.add(disease);
            if( !vaccinatedVaccine.contains(vaccine))
                vaccinatedVaccine.add(vaccine);
            if( !vaccinatedCategory.contains(category))
                vaccinatedCategory.add(category);
        }
        // result list
        List<List> result = new ArrayList<List>();
        result.add(vaccinatedDiseases);
        result.add(vaccinatedCategory);
        result.add(vaccinatedVaccine);
        return result;
    }

    public List<VaccinationStatusDto> getVaccinationStatus(DiseaseRepository diseaseRepository,
                                                           MemberVaccinationRepository vaccinationRepository,
                                                           VaccineCategoryRepository categoryRepository) {
        // Unpacking Vaccination Details
        List<List> vaccinationDetails = getVaccinationDetails(vaccinationRepository);
        List<Disease> vaccinatedDiseases = vaccinationDetails.get(0);
        List<VaccineCategory> vaccinatedCategory = vaccinationDetails.get(1);
        List<Vaccine> vaccinatedVaccine = vaccinationDetails.get(2);
        Map<Disease, VaccinationStatusDto> statusMap = new HashMap<>();
        // adds vaccinated/partially_vaccinated status for vaccinated disease
        for(Vaccine vaccine: vaccinatedVaccine) {
            // checks if mandatory vaccination is completed
            List<VaccineCategory> mandatoryCategories = vaccine.getMandatoryCategories(categoryRepository, this.getAge());
            VaccinationStatus status = VaccinationStatus.FULLY_VACCINATED;
            int vaccinationCount = 0;
            for(VaccineCategory mandatoryCategory : mandatoryCategories) {
                if( !vaccinatedCategory.contains(mandatoryCategory) )
                    status = VaccinationStatus.PARTIALLY_VACCINATED;
                else vaccinationCount++;
            }
            // Creating info dto and adding it into map
            VaccinationStatusDto dto = new VaccinationStatusDto();
            dto.setDisease(vaccine.getOfDisease());
            dto.setVaccine(vaccine);
            dto.setCount(vaccinationCount);
            dto.setStatus(status);
            statusMap.put(vaccine.getOfDisease(), dto);
        }
        // Unvaccinated Disease
        List<Disease> diseases = diseaseRepository.findAll();
        for(Disease disease: diseases) {
            if( !statusMap.containsKey(disease) ) {
                VaccinationStatusDto dto = new VaccinationStatusDto();
                dto.setCount(0);
                dto.setDisease(disease);
                dto.setStatus(VaccinationStatus.UNVACCINATED);
                statusMap.put(disease, dto);
            }
        }
        return new ArrayList<>(statusMap.values());
    }


}
