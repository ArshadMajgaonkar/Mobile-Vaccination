package com.aghs.mobilevaccination.data.model;

import com.google.common.hash.Hashing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;
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

    public void processData() {
        aadharLast4Digit = aadharId.substring(8, 12);
        aadharId = Hashing.sha512().hashString(aadharId, StandardCharsets.UTF_8).toString();
        addedAt = new Date();
    }

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
}
