package com.reservation.HotelManagement.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

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

    public VenueResponse(Long id, String venueName,String location, String venueType, int capacity) {
        this.id = id;
        this.venueName = venueName;
       this.location = location;
        this.venueType = venueType;
        this.capacity = capacity;
        this.location = location;
    }
}
