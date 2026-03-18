package com.reservation.HotelManagement.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;


}
