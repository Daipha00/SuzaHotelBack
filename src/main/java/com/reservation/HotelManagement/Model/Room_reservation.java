package com.reservation.HotelManagement.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Room_reservation extends Reservation{

    @ManyToOne(fetch = FetchType.EAGER) // Assuming you have a Venue entity
    @JoinColumn(name = "room_id")
    private Room room;
}
