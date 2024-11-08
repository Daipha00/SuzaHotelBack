package com.reservation.HotelManagement.Model;

import lombok.Data;

import java.util.List;

@Data
public class HotelResponse {
    private Long id;
    private String hotelName;
    private String location;
    private List<Long> imageIds; // List of image IDs

    public HotelResponse(Long id, String hotelName, String location, List<Long> imageIds) {
        this.id = id;
        this.hotelName = hotelName;
        this.location = location;
        this.imageIds = imageIds;
    }
}