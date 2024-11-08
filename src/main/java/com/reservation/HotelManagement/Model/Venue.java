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
    private String venueName;
    private int capacity;
    private String location;
    private String venuePackage;
    private String description;

    @Lob
    private byte[] image;

}
