package com.reservation.HotelManagement.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Room_image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
