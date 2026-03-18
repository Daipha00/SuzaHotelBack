package com.reservation.HotelManagement.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Venue_image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
