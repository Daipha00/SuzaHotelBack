package com.reservation.HotelManagement.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class FrontOfficeStaff extends User{

    // Many-to-Many relationship with Guest
    @ManyToMany
    @JoinTable(
            name = "front_staff_client",
            joinColumns = @JoinColumn(name = "front_staff_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> client;
}
