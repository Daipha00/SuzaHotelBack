package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;

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
