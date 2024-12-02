package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

@Entity
@Data
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueName;
    private String venueType;
<<<<<<< HEAD
    private int capacity;
=======
<<<<<<< HEAD

=======
>>>>>>> 21a1988b0b92524151cd78dcce1d651b451cb859


    private int capacity;

<<<<<<< HEAD

=======
>>>>>>> 21a1988b0b92524151cd78dcce1d651b451cb859
>>>>>>> 2077a4d3356b73bcdbc049596e3fc190c243d0c3
    private String location;
    private String venuePackage;
    private String description;


}

