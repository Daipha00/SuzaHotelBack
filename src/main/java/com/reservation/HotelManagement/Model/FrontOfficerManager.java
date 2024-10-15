package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FrontOfficerManager extends User{

    // Many-to-One relationship with HotelManager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_manager_id")
    private HotelManager hotelManager;

    // One-to-Many relationship with Reservation
    @OneToMany(mappedBy = "frontOfficerManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

}
