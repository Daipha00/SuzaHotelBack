package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Reservation;
import com.reservation.HotelManagement.Repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepo reservationRepo;

    // Create or update a reservation
    @PostMapping
    public ResponseEntity<Reservation> saveReservation(@RequestBody Reservation reservation) {
        Reservation savedReservation = reservationRepo.save(reservation);
        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }

    // Retrieve all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservationList = reservationRepo.findAll();
        return new ResponseEntity<>(reservationList, HttpStatus.OK);
    }

    // Retrieve a reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepo.findById(id);
        return reservation.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
}
