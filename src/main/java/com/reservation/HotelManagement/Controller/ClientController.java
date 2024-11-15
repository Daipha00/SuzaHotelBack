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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/client")

@CrossOrigin(origins = "http://localhost:3000")

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
    public ResponseEntity<String> registerClient(@RequestBody Client client) {
        // Check if the email already exists
        if (clientRepo.findByEmail(client.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        // Save the new client
        clientRepo.save(client);
        return ResponseEntity.ok("Registration successful!");
    }


<<<<<<< HEAD

=======
>>>>>>> 8469769489b95f48414b55929a3272ae5a47a941
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Client client = clientService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (client != null) {
            // Create a response object without the password
            LoginResponse response = new LoginResponse(client);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password!", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/client/login/{email}")
    public ResponseEntity<?> login(@PathVariable String email, @RequestParam String password) {
        try {
            Optional<Client> clientOptional = clientRepo.findByEmail(email);
            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();
                // Directly compare the plaintext password with the one in the database
                if (password.equals(client.getPassword())) {
                    return new ResponseEntity<>(client, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Incorrect Credentials", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>("No connection", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Boolean>> getClientByEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            boolean emailExists = clientRepo.findByEmail(email).isPresent();
            response.put("exists", emailExists);
            return new ResponseEntity<>(response, emailExists ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            response.put("exists", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

<<<<<<< HEAD
=======

>>>>>>> 8469769489b95f48414b55929a3272ae5a47a941
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
