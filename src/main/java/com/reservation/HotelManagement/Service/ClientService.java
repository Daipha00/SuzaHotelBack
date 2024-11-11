package com.reservation.HotelManagement.Service;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Reservation;
import com.reservation.HotelManagement.Repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    public List<Reservation> getReservationByClients(Long id){
        return clientRepo.getReservationByClient(id);
    }

    public Client createClient(Client client) {
        return clientRepo.save(client);
    }

    public boolean emailExists(String email) {
        System.out.println("Checking if email exists: " + email); // Debugging line
        return clientRepo.findByEmail(email).isPresent();
    }


//    public boolean login(String email, String password) {
//        Client client = clientRepo.findByEmail(email).orElse(null);
//        return client != null && client.getPassword().equals(password);
//    }

    public Client login(String email, String password) {
        Client client = clientRepo.findByEmail(email).orElse(null);
        if (client != null && client.getPassword().equals(password)) {
            return client; // Return the client object if credentials are valid
        }
        return null; // Return null if credentials are invalid
    }

}
