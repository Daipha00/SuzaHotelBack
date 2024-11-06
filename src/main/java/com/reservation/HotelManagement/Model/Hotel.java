package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

@Entity
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String hotelName;
    private String location;

    @ElementCollection
    @Lob
    private List<byte[]> images;

    @Transient // This annotation indicates that this field is not to be persisted in the database
    public List<String> getBase64Images() {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(imageBytes -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes))
                .toList();
    }
}
