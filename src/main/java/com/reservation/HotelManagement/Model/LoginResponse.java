package com.reservation.HotelManagement.Model;

public class LoginResponse {
       private Client client;

    public LoginResponse(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
