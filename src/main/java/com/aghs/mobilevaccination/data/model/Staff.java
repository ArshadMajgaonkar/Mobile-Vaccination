package com.aghs.mobilevaccination.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Staff Data Model class to define structures of staff and admin account.
 */
@Entity
@Table
public class Staff {
    @Id
    @Column(length = 30)
    private String username;
    @Column
    private String password;
    @Column
    private Date lastLogin;
    @Column
    private Date currentLogin;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthGroup role;
    @Column(unique = true, length = 11/*, nullable = false*/)
    private String mobileNumber;
    @Column(unique = true, length = 255/*, nullable = false*/)
    private String emailId;
    @Column(nullable = false)
    private LocalDateTime addedAt;
    // TODO: Self join of added by
    /*@Column(nullable = false)
    @JoinColumns(value = @JoinColumn(
            table = "staff",
            referencedColumnName = "username"))
    private Staff addedBy;*/

    public Staff() {
    }

    public Staff(
            String username,
            String password,
            AuthGroup role,
            String mobileNumber,
            String emailId,
            LocalDateTime addedAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.addedAt = addedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public AuthGroup getRole() {
        return role;
    }

    public void setRole(AuthGroup role) {
        this.role = role;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    /*public Staff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Staff addedBy) {
        this.addedBy = addedBy;
    }*/
}
