package com.reservation.HotelManagement.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import lombok.Data;



@Entity
@Data   
public class Client extends User{

    private String gender;
    private String country;
    private String city;
    private int zipcode;
    private String passport;

}
