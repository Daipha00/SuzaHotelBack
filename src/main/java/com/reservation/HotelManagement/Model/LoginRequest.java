package com.reservation.HotelManagement.Model;

public class LoginRequest {
    private String userName;
    private String userPassword;

    // Default constructor
    public LoginRequest() {}

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}