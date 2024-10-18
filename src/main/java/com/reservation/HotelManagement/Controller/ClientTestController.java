package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientTestController {

    @Autowired
    private ClientRepo clientRepository;

    // Create a new client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientRepository.save(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Get a specific client by ID
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {

        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (!clientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(clientOptional.get(), HttpStatus.OK);
    }

    // Get all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // Delete a client by ID
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {

        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (!clientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        clientRepository.deleteById(clientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update client information
    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client updatedClient) {

        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if (!clientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Client existingClient = clientOptional.get();

        // Update the client details
        existingClient.setCity(updatedClient.getCity());
        existingClient.setCountry(updatedClient.getCountry());
        existingClient.setZipcode(updatedClient.getZipcode());

        Client savedClient = clientRepository.save(existingClient);

        return new ResponseEntity<>(savedClient, HttpStatus.OK);
    }
}
