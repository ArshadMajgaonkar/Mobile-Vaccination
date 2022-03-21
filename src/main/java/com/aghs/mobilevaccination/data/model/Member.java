package com.aghs.mobilevaccination.data.model;

import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.model.vaccine.VaccinationStatus;
import com.aghs.mobilevaccination.data.model.vaccine.Vaccine;
import com.aghs.mobilevaccination.data.repository.DiseaseRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineRepository;
import com.google.common.hash.Hashing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
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
        return LocalDate.now().getYear() - this.getAge();
    }

    public List<Vaccine> getEligibleVaccines(DiseaseRepository diseaseRepository,
                                             MemberVaccinationRepository vaccinationRepository,
                                             VaccineRepository vaccineRepository) {
        Set<Vaccine> eligibleVaccines = new HashSet<>();
        Set<Disease> vaccinatedDiseases = new HashSet<>();
        List<MemberVaccination> vaccinations = vaccinationRepository.findByRecipient(this);
        for(MemberVaccination vaccination: vaccinations) {
            if(vaccination.getStatus() == VaccinationStatus.VACCINATED) {
                eligibleVaccines.add(vaccination.getVaccineDrive().getVaccine());
                vaccinatedDiseases.add(vaccination.getVaccineDrive().getVaccine().getOfDisease());
            }
        }
        List<Disease> diseases = diseaseRepository.findAll();
        for(Disease disease: diseases) {
            if(!vaccinatedDiseases.contains(disease)) {
                List<Vaccine> vaccines = vaccineRepository.findByOfDisease(disease);
                eligibleVaccines.addAll(vaccines);
            }
        }
        return new ArrayList<Vaccine>(eligibleVaccines);
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




}
