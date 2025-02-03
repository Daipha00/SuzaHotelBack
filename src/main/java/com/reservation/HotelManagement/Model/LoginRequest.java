package com.reservation.HotelManagement.Model;

public class LoginRequest {
    private String email;
    private String userPassword;

    // Default constructor
    public LoginRequest() {}

    // Getters and Setters


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}