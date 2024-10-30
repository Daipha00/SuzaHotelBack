package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Venue;
import com.reservation.HotelManagement.Model.Venue_reservation;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.VenueRepo;
import com.reservation.HotelManagement.Repository.VenueReservationRepository;
import com.reservation.HotelManagement.Service.VenueReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(originPatterns = "*")

@RequestMapping("/venue_reservation")
public class VenueReservationController {

    @Autowired
    private VenueReservationService venueReservationService;

    @Autowired
    private VenueRepo venueRepo;
    @Autowired
    private VenueReservationRepository venueReservationRepository;

    @Autowired
    private ClientRepo clientRepo;

    // Post a new venue reservation
    @PostMapping
    @ResponseBody
    public Venue_reservation createNewReservation(@RequestBody Venue_reservation reservation,
                                            @RequestParam Long clientId,
                                            @RequestParam Long venueId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        reservation.setClient(client); // Set the client for the reservation
        reservation.setVenue(venue); // Set the venue for the reservation

        return venueReservationRepository.save(reservation);
    }


    // Get all venue reservations
    @GetMapping
    public List<Venue_reservation> getAllVenueReservations() {
        return venueReservationService.getAllVenueReservations();
    }

    // Get venue reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Venue_reservation> getVenueReservationById(@PathVariable Long id) {
        return venueReservationService.getVenueReservationById(id);
    }

    // Delete venue reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenueReservation(@PathVariable Long id) {
        venueReservationService.deleteVenueReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Update venue reservation
    @PutMapping("/{id}")
    public ResponseEntity<Venue_reservation> updateVenueReservation(@PathVariable Long id, @RequestBody Venue_reservation updatedVenueReservation) {
        return venueReservationService.updateVenueReservation(id, updatedVenueReservation);
    }
}
