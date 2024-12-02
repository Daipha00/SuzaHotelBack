package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Data
public class RoomResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private int pax;
    private String description;

    private List<Long> imageIds;

    public RoomResponse(Long id, String roomType, int pax, String description, List<Long> imageIds) {
        this.id = id;
        this.roomType = roomType;
        this.pax = pax;
        this.description = description;
        this.imageIds = imageIds;
    }
}
