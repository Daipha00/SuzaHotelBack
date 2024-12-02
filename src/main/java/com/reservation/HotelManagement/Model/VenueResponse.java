package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;



@Data
public class VenueResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueName;
    private String venueType;
    private int capacity;
    private String location;
    private String venuePackage;
    private String description;

    private List<Long> imageIds; // List of image IDs


    public VenueResponse(Long id, String venueName, String venuePackage, String venueType,
                         int capacity, String description, String location, List<Long> imageIds) {
        this.id = id;
        this.venueName = venueName;
        this.venuePackage = venuePackage;
        this.venueType = venueType;
        this.capacity = capacity;
        this.description = description;
        this.location = location;
        this.imageIds = imageIds;
    }
}
