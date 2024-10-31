package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Room;
import com.reservation.HotelManagement.Model.Venue;
import com.reservation.HotelManagement.Repository.VenueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/venue")
@CrossOrigin(originPatterns = "*")

public class VenueController {

    @Autowired
    private VenueRepo venueRepo;

    @PostMapping
    public ResponseEntity<Venue> createVenue(
            @RequestParam("venueType") String venueType,
            @RequestParam("capacity") int capacity,
            @RequestParam("location") String location,
            @RequestParam("price") Double price,

            @RequestParam("venuePackage") String venuePackage,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        // Create a new Trial instance and set fields
        Venue venue = new Venue();
        venue.setVenueType(venueType);
        venue.setCapacity(capacity);
        venue.setLocation(location);
        venue.setPrice(price);

        venue.setVenuePackage(venuePackage);
        venue.setDescription(description);

        // Set the image as a byte array
        venue.setImage(imageFile.getBytes());

        // Save the trial entity to the database
        Venue savedVenue = venueRepo.save(venue);

        // Return a response entity with the saved trial
        return ResponseEntity.ok(savedVenue);
    }

    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenue() {
        List<Venue> venues = venueRepo.findAll();
        return ResponseEntity.ok(venues);
    }

    // GET method to retrieve a single trial by ID
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return venueRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updatedVenue(
            @PathVariable Long id,
            @RequestParam("venueType") String venueType,
            @RequestParam("capacity") int capacity,
            @RequestParam("location") String location,
            @RequestParam("price") Double price,

            @RequestParam("venuePackage") String venuePackage,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        // Find the trial by ID
        Optional<Venue> existingVenueOpt = venueRepo.findById(id);
        if (!existingVenueOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Update the trial's data
        Venue existingVenue = existingVenueOpt.get();
        existingVenue.setVenueType(venueType);
        existingVenue.setLocation(location);
        existingVenue.setPrice(price);

        existingVenue.setDescription(description);
        existingVenue.setVenuePackage(venuePackage);
        existingVenue.setCapacity(capacity);

        // If a new image is provided, update the image
        if (imageFile != null && !imageFile.isEmpty()) {
            existingVenue.setImage(imageFile.getBytes());
        }

        // Save the updated trial
        Venue updatedVenue = venueRepo.save(existingVenue);
        return ResponseEntity.ok(updatedVenue);
    }

    // DELETE method to delete a trial by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        Optional<Venue> existingVenueOpt = venueRepo.findById(id);
        if (!existingVenueOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        venueRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
