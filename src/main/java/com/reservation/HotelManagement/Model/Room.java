package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Room{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private int pax;
    private String description;


    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // Foreign key to Hotel
    private Hotel hotel;


}
