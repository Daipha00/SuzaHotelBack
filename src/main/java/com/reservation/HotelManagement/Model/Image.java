package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;

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
