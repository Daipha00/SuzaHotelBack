package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.VenueRepo;
import com.reservation.HotelManagement.Repository.VenueReservationRepository;
import com.reservation.HotelManagement.Service.EmailService;
import com.reservation.HotelManagement.Service.VenueReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin(originPatterns = "*")

@RequestMapping("/venue_reservation")
public class VenueReservationController {

    @Autowired
    private VenueReservationService venueReservationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VenueRepo venueRepo;
    @Autowired
    private VenueReservationRepository venueReservationRepository;

    @Autowired
    private ClientRepo clientRepo;

    // Post a new venue reservation
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createNewVenueReservation(@RequestBody Venue_reservation reservation,
                                                            @RequestParam Long clientId,
                                                            @RequestParam Long venueId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Venue venue = venueRepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        // Check for existing reservations
        List<Venue_reservation> existingReservations = venueReservationRepository.findOverlappingReservations(
                venueId, reservation.getCheck_in(), reservation.getCheck_out());

        if (!existingReservations.isEmpty()) {
            return ResponseEntity.badRequest().body("The venue is already booked between these dates.");
        }

        reservation.setClient(client); // Set the client for the reservation
        reservation.setVenue(venue); // Set the venue for the reservation
        reservation.setStatus("Pending"); // Set the default status to Pending

        venueReservationRepository.save(reservation);
        return ResponseEntity.ok("Venue reservation created successfully.");
    }

    // Get all reservations for a specific client along with the venue details
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Venue_reservation>> getReservationsByClientId(@PathVariable Long clientId) {
        // Retrieve the client by their ID
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Retrieve all reservations made by the client along with the associated venue details
        List<Venue_reservation> reservations = venueReservationRepository.findByClientIdWithVenue(clientId);

        // If no reservations found, return a not found response
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Return the list of reservations with venue details
        return ResponseEntity.ok(reservations);
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

    @GetMapping("/check-in")
    public ResponseEntity<List<Venue_reservation>> getReservationsByCheckInDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate check_in) {
        System.out.println("Checking reservations for date: " + check_in);

        // Use the custom query method
        List<Venue_reservation> reservations = venueReservationRepository.findByCheckIn(check_in);

        System.out.println("Found reservations: " + reservations.size());
        return ResponseEntity.ok(reservations);
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

    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelVenueReservation(@PathVariable Long reservationId) {
        Venue_reservation reservation = venueReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if ("Confirmed".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation cannot be cancelled as it has already been confirmed.");
        }

        if ("Cancelled".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation is already cancelled.");
        }

        reservation.setStatus("Cancelled"); // Update the status to Cancelled
        venueReservationRepository.save(reservation);
        return ResponseEntity.ok("Reservation cancelled successfully.");
    }
}

//    @PatchMapping("{id}/status")
//    public ResponseEntity<Venue_reservation> updateStatus(@PathVariable long id) {
//        Venue_reservation venueReservation = venueReservationRepository.findById(id).orElseThrow();
//        if (venueReservation.getStatus().equals("Pending")) {
//            venueReservation.setStatus("Checked-in");
//        } else {
//            venueReservation.setStatus("Checked-out");
//        }
//        venueReservationRepository.save(venueReservation);
//        return ResponseEntity.ok(venueReservation);
//    }}
