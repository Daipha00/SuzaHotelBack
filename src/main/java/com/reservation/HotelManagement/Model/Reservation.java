package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate check_in;
    private LocalDate check_out;
    private LocalTime arrival_time;
    private String special_request;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    // Many-to-One relationship with Room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    // Many-to-One relationship with Venue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    // Many-to-One relationship with Front Office Manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "front_office_manager_id")
    private FrontOfficerManager frontOfficerManager;
}
