package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Room_reservation extends Reservation{

    @ManyToOne(fetch = FetchType.EAGER) // Assuming you have a Venue entity
    @JoinColumn(name = "room_id")
    private Room room;
}
