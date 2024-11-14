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
<<<<<<< HEAD
      private int capacity;
=======

    private int capacity;
>>>>>>> 7446e4ee7301f5f71d0e54149ae8a637e467dcda
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
