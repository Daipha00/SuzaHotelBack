package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Venue_reservation extends Reservation{

    @ManyToOne(fetch = FetchType.EAGER) // Assuming you have a Venue entity
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
