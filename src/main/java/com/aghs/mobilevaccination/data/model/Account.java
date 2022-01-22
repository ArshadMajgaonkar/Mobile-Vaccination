package com.aghs.mobilevaccination.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Account Data Model class for logging in General Users.
 * One Account can be linked to multiple Member objects.
 */
@Entity
@Table
public class Account {
    @Id
    @Column(length = 11)
    private String mobileNumber;
    @Column
    private String otp;
    @Column
    private String email;
    // Last Login feature
    @Column
    private Date lastLogin;
    @Column
    private Date currentLogin;

    public Account() {
    }

    public Account(String mobileNumber, String otp, String email, Date lastLogin, Date currentLogin) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.email = email;
        this.lastLogin = lastLogin;
        this.currentLogin = currentLogin;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCurrentLogin() {
        return currentLogin;
    }

    public void setCurrentLogin(Date currentLogin) {
        this.currentLogin = currentLogin;
    }

}
