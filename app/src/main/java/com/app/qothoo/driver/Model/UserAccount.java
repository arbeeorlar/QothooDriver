package com.app.qothoo.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbookpro on 06/07/2017.
 */

public class UserAccount {


    @SerializedName("countryID")
    @Expose
    private Integer countryID;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("homeAddress")
    @Expose
    private String homeAddress;
    @SerializedName("homeLat")
    @Expose
    private Double homeLat;
    @SerializedName("homeLong")
    @Expose
    private Double homeLong;
    @SerializedName("personalReferralCode")
    @Expose
    private String personalReferralCode;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("registeredDate")
    @Expose
    private String registeredDate;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("workAddress")
    @Expose
    private String workAddress;
    @SerializedName("workLat")
    @Expose
    private Double workLat;
    @SerializedName("workLong")
    @Expose
    private Double workLong;

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        } else {
            this.email = null;
        }
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        if (homeAddress != null) {
            this.homeAddress = homeAddress;
        } else {
            this.homeAddress = null;
        }
    }

    public Double getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(Double homeLat) {
        if (homeLat != 0) {
            this.homeLat = homeLat;
        } else {
            homeLat = 0.0;
        }
    }

    public Double getHomeLong() {
        return homeLong;
    }

    public void setHomeLong(Double homeLong) {
        if (homeLong != 0) {
            this.homeLong = homeLong;
        } else {
            this.homeLong = 0.0;
        }
    }

    public String getPersonalReferralCode() {
        return personalReferralCode;
    }

    public void setPersonalReferralCode(String personalReferralCode) {
        if (personalReferralCode != null) {
            this.personalReferralCode = personalReferralCode;
        } else {
            this.personalReferralCode = null;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo != null) {
            this.photo = photo;
        } else {
            this.photo = null;
        }
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        if (registeredDate != null) {
            this.registeredDate = registeredDate;
        } else {
            this.registeredDate = null;
        }
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        if (workAddress != null) {
            this.workAddress = workAddress;
        } else {
            this.workAddress = null;
        }
    }

    public Double getWorkLat() {
        return workLat;
    }

    public void setWorkLat(Double workLat) {
        if (workLat != 0) {
            this.workLat = workLat;
        } else {
            this.workLat = 0.0;
        }
    }

    public Double getWorkLong() {
        return workLong;
    }

    public void setWorkLong(Double workLong) {
        if (workLong != 0) {
            this.workLong = workLong;
        } else {
            this.workLong = 0.0;
        }
    }

}

