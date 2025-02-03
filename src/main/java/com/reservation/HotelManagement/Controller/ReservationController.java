package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.ReservationRepo;
import com.reservation.HotelManagement.Repository.RoomReservationRepository;
import com.reservation.HotelManagement.Repository.VenueReservationRepository;
import com.reservation.HotelManagement.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")

@CrossOrigin(origins = "*")

public class ReservationController {

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoomReservationRepository roomReservationRepository;

    @Autowired
    private VenueReservationRepository venueReservationRepository;

    @Autowired
    private ClientRepo clientRepo;

    @PostMapping
    @ResponseBody
    public Reservation createNewReservation(@RequestBody Reservation reservation, @RequestParam Long clientId) {
        Client client = clientRepo.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        reservation.setClient(client);
        return reservationRepo.save(reservation);
    }

    @GetMapping
    public List<Reservation> getReservationsWithClientDetails(){
        List<Reservation> reservations = reservationRepo.findAll();
        for (Reservation reservation : reservations) {
            Client client = reservation.getClient();
            // You can add additional client details to the response if needed
        }
        return reservations;
    }

    // Get room or venue details by reservation ID for a particular client
    @GetMapping("/details/{reservationId}")
    public ResponseEntity<?> getRoomOrVenueDetailsByReservationId(@PathVariable Long reservationId) {
        // Fetch the reservation by ID
        Optional<Reservation> reservationOptional = reservationRepo.findById(reservationId);

        if (!reservationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        Reservation reservation = reservationOptional.get();

        // Fetch room or venue details based on the reservation type
        if (reservation instanceof Room_reservation) {
            Room_reservation roomReservation = (Room_reservation) reservation;
            Room room = roomReservation.getRoom();

            // Return room details with the associated hotel details
            RoomResponse roomResponse = new RoomResponse(
                    room.getId(),
                    room.getRoomType(),
                    room.getPax(),
                    room.getPrice(),
                    room.getDescription(),
                   room.getHotel() // Pass the full Hotel object here
                    // Assuming you have image IDs in the room entity
            );

            return ResponseEntity.ok(roomResponse);
        } else if (reservation instanceof Venue_reservation) {
            Venue_reservation venueReservation = (Venue_reservation) reservation;
            Venue venue = venueReservation.getVenue();

            // Return venue details in a structured format (you can modify this as needed)
            return ResponseEntity.ok(new VenueResponse(
                    venue.getId(),
                    venue.getVenueName(),
                    venue.getLocation(),
                    venue.getVenueType(),
                    venue.getCapacity()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation type");
        }
    }

    @GetMapping("/client-by-reservation/{reservationId}")
    public ResponseEntity<?> getClientByReservationId(@PathVariable Long reservationId) {
        // Find the reservation by ID
        Optional<Reservation> reservationOptional = reservationRepo.findById(reservationId);

        if (!reservationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        Reservation reservation = reservationOptional.get();

        // Get the associated client
        Client client = reservation.getClient();

        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No client associated with this reservation");
        }

        // Return client details
        return ResponseEntity.ok(client);
    }



    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getAllReservationsByClient(@PathVariable Long clientId) {
        // Ensure the client exists
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Retrieve room reservations for the client
        List<Room_reservation> roomReservations = roomReservationRepository.findByClientId(clientId);

        // Retrieve venue reservations for the client
        List<Venue_reservation> venueReservations = venueReservationRepository.findByClientId(clientId);

        // Combine the results into a single list
        List<Reservation> allReservations = new ArrayList<>();
        allReservations.addAll(roomReservations);
        allReservations.addAll(venueReservations);

        return ResponseEntity.ok(allReservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // Update a reservation
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        Optional<Reservation> existingReservation = reservationRepo.findById(id);
        if (existingReservation.isPresent()) {
            reservation.setId(id); // Ensure the correct ID is being updated
            Reservation updatedReservation = reservationRepo.save(reservation);
            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        if (reservation.isPresent()) {
            reservationRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        // Fetch the reservation by ID
        Optional<Reservation> reservationOptional = reservationRepo.findById(reservationId);
        if (!reservationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        Reservation reservation = reservationOptional.get();

        // Check if the reservation is confirmed or already cancelled
        if ("Confirmed".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation cannot be cancelled as it has already been confirmed.");
        }

        if ("Cancelled".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation is already cancelled.");
        }

        // Update the status to 'Cancelled'
        reservation.setStatus("Cancelled");
        reservationRepo.save(reservation);

        // Check the reservation type and return appropriate response
        if (reservation instanceof Room_reservation) {
            return ResponseEntity.ok("Room reservation cancelled successfully.");
        } else if (reservation instanceof Venue_reservation) {
            return ResponseEntity.ok("Venue reservation cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation type");
        }
    }

    @PutMapping("/confirm/{reservationId}")
    public ResponseEntity<String> confirmReservation(@PathVariable Long reservationId) {
        // Fetch the reservation by ID
        Optional<Reservation> reservationOptional = reservationRepo.findById(reservationId);
        if (!reservationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        Reservation reservation = reservationOptional.get();

        // Check if the reservation is already cancelled
        if ("Cancelled".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Cannot confirm a cancelled reservation.");
        }

        // Check if the reservation is already confirmed
        if ("Confirmed".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation is already confirmed.");
        }

        // Update the status to 'Confirmed'
        reservation.setStatus("Confirmed");
        reservationRepo.save(reservation);

        // Send confirmation email to the client
        emailService.sendConfirmationEmail(
                reservation.getClient().getEmail(),
                reservation.getClient().getUserFirstName(),
                reservation.getId()
        );

        // Check the reservation type and return appropriate response
        if (reservation instanceof Room_reservation) {
            return ResponseEntity.ok("Room reservation confirmed successfully.");
        } else if (reservation instanceof Venue_reservation) {
            return ResponseEntity.ok("Venue reservation confirmed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation type");
        }
    }

}

