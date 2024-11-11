package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.UserRepo;
import com.reservation.HotelManagement.Service.ClientService;
import com.reservation.HotelManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")

@CrossOrigin(originPatterns = "*")

public class ClientController {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Client client) {
        if (clientService.emailExists(client.getEmail())) {
            return new ResponseEntity<>("Email already exists!", HttpStatus.CONFLICT);
        }
        Client savedClient = clientService.createClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = clientService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            return new ResponseEntity<>("Login successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password!", HttpStatus.UNAUTHORIZED);
        }
    }

    // Retrieve all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientRepo.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // Retrieve a client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepo.findById(id);
        return client.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/reservation")
    public List<Reservation> getReservationByClientId(@PathVariable("id") Long id){
           return clientService.getReservationByClients(id);
    }

    // Update a client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Optional<Client> existingClient = clientRepo.findById(id);
        if (existingClient.isPresent()) {
            client.setId(id); // Ensure the correct ID is being updated
            Client updatedClient = clientRepo.save(client);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a client by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Optional<Client> client = clientRepo.findById(id);
        if (client.isPresent()) {
            clientRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
