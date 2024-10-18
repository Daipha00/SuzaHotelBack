package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Venue_reservation;
import com.reservation.HotelManagement.Service.VenueReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venue-reservation")
public class VenueReservationController {

    @Autowired
    private VenueReservationService venueReservationService;

    // Post a new venue reservation
    @PostMapping
    public Venue_reservation createVenueReservation(@RequestBody Venue_reservation venueReservation) {
        return venueReservationService.createVenueReservation(venueReservation);
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
