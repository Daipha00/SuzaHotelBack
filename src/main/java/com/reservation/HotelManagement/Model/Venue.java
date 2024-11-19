package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Data
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueName;
    private String venueType;



    private int capacity;


    private String location;
    private String venuePackage;
    private String description;

    @Lob
    private byte[] image;


    // Method to get the image as a Base64 string
    public String getImageAsBase64() {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }

    public void setImageAsBase64(String imageAsBase64) {
    }
}
    // Many-to-One relationship with Guest
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id")
//    private Client client;
//}
