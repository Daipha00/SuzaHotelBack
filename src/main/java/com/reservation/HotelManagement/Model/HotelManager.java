package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class HotelManager extends User{


    // One-to-Many relationship with FrontOfficeManager
    @OneToMany(mappedBy = "hotelManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FrontOfficerManager> frontOfficeManagers;

}
