package me.morasquad.wicard.models;

/**
 * Created by Sandun Isuru Niraj on 15/05/2018.
 */

public class MyWicard {

    private String email, address, mobileNumber, fullName;

    public MyWicard(String email, String address, String mobileNumber, String fullName) {
        this.email = email;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
    }

    public MyWicard() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
