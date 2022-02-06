package com.aghs.mobilevaccination.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Account Data Model class for logging in General Users.
 * One Account can be linked to multiple Member objects.
 */
@Entity
@Table
public class Account {
    @Id
    @Column(length = 13)
    private String mobileNumber;
    @Column
    private String otp;
    // OTP Generation Time
    @Column
    private Date otpGeneratedAt;
    @Column
    private String email;
    // Last Login feature
    @Column
    private Date lastLogin;
    @Column
    private Date currentLogin;

    private static final String COUNTRY_CODE = "+91";
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRE_IN_MIN = 10;

    public Account() {
    }

    public Account(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public Date getOtpGeneratedAt() {
        return otpGeneratedAt;
    }

    public void setOtpGeneratedAt(Date otpGeneratedAt) {
        this.otpGeneratedAt = otpGeneratedAt;
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

    /**
     * Adds Country Code(+91) to mobileNumber attribute if code isn't present in the number.
     */
    public void addCountryCodeToMobileNumberIfNotPresent() {
        if(!mobileNumber.startsWith("+91"))
            mobileNumber = COUNTRY_CODE + mobileNumber;
    }



    public void generateOtp() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i< Account.OTP_LENGTH; i++) {
            otp.append(String.valueOf(random.nextInt(10)));
        }
        this.otp = otp.toString();
    }

    /**
     * Performs server-side validation of mobileNumber attribute with or without country code.
     * @return boolean true if mobileNumber is valid or else returns false.
     */
    public boolean isMobileNumberValid() {
        return mobileNumber != null
                && !mobileNumber.isBlank()
                && (Pattern.matches("^[6-9][0-9]{9}$", mobileNumber)                                                // checks if mobile number is of length 10 and numeric without country code
                        || Pattern.matches("^\\+91[6-9][0-9]{9}$", mobileNumber));                                  // checks if mobile number is valid with country code
    }

    /**
     * Performs Sever-side of OTP. Checks if OTP is of specified length and is numeric.
     * @return boolean true if otp is valid or else returns false.
     */
    public boolean isOtpValid() {
        return Pattern.matches(String.format("^[0-9]{%s}$", OTP_LENGTH), otp);
    }

    /**
     * Checks expiration time of the otp
     *
     * @return boolean true is otp is expired else false is returned.
     */
    public boolean isOtpExpired() {
        LocalDateTime expiry = otpGeneratedAt.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime()
                .plusMinutes(OTP_EXPIRE_IN_MIN);
        return expiry.compareTo(LocalDateTime.now()) < 0;
    }


}
