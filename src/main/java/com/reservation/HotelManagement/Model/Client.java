package com.reservation.HotelManagement.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userFirstName;
    private String userLastName;
    private String address;
    private String email;
    private String phoneNumber;
            private String country;
            private String city;
            private int zipcode;

    // Many-to-Many relationship with Front Office Staff
    @ManyToMany(mappedBy = "client")
    private List<FrontOfficeStaff> frontOfficeStaff;



}
