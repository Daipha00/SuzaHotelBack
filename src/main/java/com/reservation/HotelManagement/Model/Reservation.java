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
    private LocalTime depature_time;
    private int numberOfGuests;
    private String special_request;
    private  String status;
    private String confirmation;

    @ManyToOne(fetch = FetchType.EAGER) // Keep this to establish the relationship
    @JoinColumn(name = "client_id")
    private Client client;



}
