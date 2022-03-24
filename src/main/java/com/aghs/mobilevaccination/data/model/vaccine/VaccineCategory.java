package com.aghs.mobilevaccination.data.model.vaccine;

import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.Staff;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.VaccineCategoryRepository;
import org.springframework.util.AutoPopulatingList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class VaccineCategory {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @ManyToOne(optional = false)
    private Vaccine vaccine;
    @Column
    private Integer minAgeLimit;
    @Column
    private Integer maxAgeLimit;
    @Column
    private Boolean  isMandatory;
    @ManyToOne
    private VaccineCategory prerequisite;
    @Column(nullable = false)
    private Date addedAt;
    @ManyToOne(optional = false)
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

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public Integer getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(Integer minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public Integer getMaxAgeLimit() {
        return maxAgeLimit;
    }

    public void setMaxAgeLimit(Integer maxAgeLimit) {
        this.maxAgeLimit = maxAgeLimit;
    }

    public Boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public VaccineCategory getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(VaccineCategory prerequisite) {
        this.prerequisite = prerequisite;
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

    @Override
    public String toString() {
        return "VaccineCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vaccine=" + vaccine +
                ", minAgeLimit=" + minAgeLimit +
                ", maxAgeLimit=" + maxAgeLimit +
                ", isMandatory=" + isMandatory +
                ", prerequisite=" + prerequisite +
                ", addedAt=" + addedAt +
                ", addedBy=" + addedBy +
                '}';
    }

    // Method

    public List<VaccineCategory> getEligibleCategory(Vaccine vaccine, int age, VaccineCategoryRepository categoryRepository) {
        List<VaccineCategory> eligibleCategories = new ArrayList<>();
        /*for() {

        }*/
        return eligibleCategories;
    }

    /*public static VaccineCategory getNextCategory(Member member,
                                           Vaccine vaccine,
                                           MemberVaccinationRepository vaccinationRepository,
                                           VaccineCategoryRepository categoryRepository) {
        VaccineCategory latestCategoryOfVaccine = member.getLatestVaccinatedCategory(vaccine,vaccinationRepository);
        List<VaccineCategory> vaccineCategories = categoryRepository.findByPrerequisite(latestCategoryOfVaccine);
        List<VaccineCategory> possibleCategory = new ArrayList<>();
        for(VaccineCategory category: vaccineCategories) {
            if(category.isEligible(member.getAge())) {

            }
        }
    }*/

    public static List<VaccineCategory> getNextPossibleCategory(Member member,
                                                                Vaccine vaccine,
                                                                List<MemberVaccination> vaccinations,
                                                                VaccineCategoryRepository categoryRepository) {
        VaccineCategory latestCategoryOfVaccine = member.getLatestVaccinatedCategory(vaccine, vaccinations);
        List<VaccineCategory> vaccineCategories = categoryRepository.findByVaccineAndPrerequisite(vaccine, latestCategoryOfVaccine);
        List<VaccineCategory> possibleCategory = new ArrayList<>();
        for(VaccineCategory category: vaccineCategories) {
            if( category.isEligible(member.getAge()) )
                possibleCategory.add(category);
        }
        return possibleCategory;
    }

    public boolean isEligible(int age) {
        return this.getMinAgeLimit() <= age && age <= this.getMaxAgeLimit();
    }
}
