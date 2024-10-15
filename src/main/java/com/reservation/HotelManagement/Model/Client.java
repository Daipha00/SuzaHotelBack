package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Client extends User{

            private String country;
            private String city;
            private int zipcode;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    // One-to-Many relationship with Room
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;

    // One-to-Many relationship with Venue
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venue> venues;

    // Many-to-Many relationship with Front Office Staff
    @ManyToMany(mappedBy = "client")
    private List<FrontOfficeStaff> frontOfficeStaff;

}
