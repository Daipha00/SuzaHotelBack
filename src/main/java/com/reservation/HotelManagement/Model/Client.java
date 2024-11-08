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
    private String age;
    private String password;
    private String gender;
    private String email;
    private String country;
    private String city;
    private String address;
    private int zipcode;
    private String passport;
    private String phoneNumber;





    // Many-to-Many relationship with Front Office Staff
    @ManyToMany(mappedBy = "client")
    private List<FrontOfficeStaff> frontOfficeStaff;



}
