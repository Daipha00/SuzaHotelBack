package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

@Entity
@Data
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueName;
    private String venueType;
    private int capacity;
    private String location;
    private String venuePackage;
    private String description;


}

