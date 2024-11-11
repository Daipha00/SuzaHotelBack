package com.reservation.HotelManagement.Model;

public class LoginResponse {
    private String userFirstName;
    private String userLastName;
    private String password;
    private String address;
    private String age;
    private String gender;
    private String email;
    private String country;
    private String city;
    private int zipcode;
    private String passport;
    private String phoneNumber;

    public LoginResponse(String userFirstName, String userLastName, String password, String address, String age,
                         String gender, String email,
                         String country, String city, int zipcode, String passport, String phoneNumber) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.password = password;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.passport = passport;
        this.phoneNumber = phoneNumber;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
