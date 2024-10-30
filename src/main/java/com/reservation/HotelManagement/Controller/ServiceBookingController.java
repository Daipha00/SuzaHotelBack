package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.ServiceBooking;
import com.reservation.HotelManagement.Repository.ServiceBookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
public class ServiceBookingController {

    @Autowired
    private ServiceBookingRepo serviceBookingRepo;

    // Create a new booking
    @PostMapping
    public ResponseEntity<ServiceBooking> createBooking(@RequestBody ServiceBooking serviceBooking) {
        ServiceBooking savedBooking = serviceBookingRepo.save(serviceBooking);
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<ServiceBooking>> getAllBookings() {
        List<ServiceBooking> bookings = serviceBookingRepo.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceBooking> getBookingById(@PathVariable Long id) {
        Optional<ServiceBooking> booking = serviceBookingRepo.findById(id);
        return booking.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update a booking
    @PutMapping("/{id}")
    public ResponseEntity<ServiceBooking> updateBooking(@PathVariable Long id, @RequestBody ServiceBooking updatedBooking) {
        if (!serviceBookingRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        updatedBooking.setId(id); // Ensure the ID is set for the update
        ServiceBooking savedBooking = serviceBookingRepo.save(updatedBooking);
        return ResponseEntity.ok(savedBooking);
    }

    // Delete a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (!serviceBookingRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        serviceBookingRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}