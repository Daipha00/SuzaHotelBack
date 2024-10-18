package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueType;
    private int capacity;
    private String location;
    private double price;
    private  String availabilityStatus;
    private String venuePackage;
    private String description;

    @Lob
    private byte[] image;

    // Many-to-One relationship with Guest
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id")
//    private Client client;
}
