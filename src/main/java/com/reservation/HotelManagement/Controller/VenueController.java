package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.VenueImageRepo;
import com.reservation.HotelManagement.Repository.VenueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/venue")
@CrossOrigin(originPatterns = "*")

public class VenueController {

    @Autowired
    private VenueRepo venueRepo;

    @Autowired
    private VenueImageRepo venueImageRepo;



    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(
            @RequestParam("venueName") String venueName,
            @RequestParam("venueType") String venueType,
            @RequestParam("capacity") int capacity,
            @RequestParam("location") String location,
            @RequestParam("venuePackage") String venuePackage,
            @RequestParam("description") String description,
            @RequestParam("images") MultipartFile[] images) {

        // Create a new hotel entity
        Venue venue = new Venue();
        venue.setVenueName(venueName);
        venue.setVenueType(venueType);
        venue.setVenuePackage(venuePackage);
        venue.setCapacity(capacity);
        venue.setDescription(description);
        venue.setLocation(location);

        // Save the hotel to the database first
        Venue savedVenue = venueRepo.save(venue);

        // List to hold the saved image IDs
        List<Long> imageIds = new ArrayList<>();

        // Process the uploaded images and save them
        for (MultipartFile imageFile : images) {
            try {
                // Create a new Image entity
                Venue_image image = new Venue_image();
                image.setImage(imageFile.getBytes()); // Store the image data

                // Set the hotel reference to the saved hotel
                image.setVenue(savedVenue);

                // Save the image to the database
                Venue_image savedImage = venueImageRepo.save(image);
                imageIds.add(savedImage.getId()); // Add the saved image ID to the list
            } catch (Exception e) {
                // Handle exceptions (e.g., log the error)
                e.printStackTrace();
            }
        }

        // Create a response object including the ID and image IDs
        VenueResponse venueResponse = new VenueResponse(savedVenue.getId(), savedVenue.getVenueName(),
                savedVenue.getVenuePackage(),savedVenue.getVenueType(),savedVenue.getCapacity(),savedVenue.getDescription(),
                savedVenue.getLocation(), imageIds);
        return new ResponseEntity<>(venueResponse, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<Venue> venues = venueRepo.findAll();
        List<VenueResponse> venueResponses = venues.stream().map(venue -> {
            List<Long> imageIds = venueImageRepo.findByVenueId(venue.getId())
                    .stream()
                    .map(Venue_image::getId)
                    .collect(Collectors.toList());

            return new VenueResponse(
                    venue.getId(),
                    venue.getVenueName(),
                    venue.getVenuePackage(),
                    venue.getVenueType(),
                    venue.getCapacity(),
                    venue.getDescription(),
                    venue.getLocation(),
                    imageIds
            );
        }).collect(Collectors.toList());

        return new ResponseEntity<>(venueResponses, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        Venue venue = venueRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        List<Long> imageIds = venueImageRepo.findByVenueId(venue.getId())
                .stream()
                .map(Venue_image::getId)
                .collect(Collectors.toList());

        VenueResponse venueResponse = new VenueResponse(
                venue.getId(),
                venue.getVenueName(),
                venue.getVenuePackage(),
                venue.getVenueType(),
                venue.getCapacity(),
                venue.getDescription(),
                venue.getLocation(),
                imageIds
        );

        return new ResponseEntity<>(venueResponse, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> updateVenue(
            @PathVariable Long id,
            @RequestParam("venueName") String venueName,
            @RequestParam("venueType") String venueType,
            @RequestParam("capacity") int capacity,
            @RequestParam("location") String location,
            @RequestParam("venuePackage") String venuePackage,
            @RequestParam("description") String description,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        Venue venue = venueRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        venue.setVenueName(venueName);
        venue.setVenueType(venueType);
        venue.setVenuePackage(venuePackage);
        venue.setCapacity(capacity);
        venue.setDescription(description);
        venue.setLocation(location);

        Venue updatedVenue = venueRepo.save(venue);

        // If new images are provided, replace the old ones
        if (images != null && images.length > 0) {
            // Delete old images
            venueImageRepo.findByVenueId(id).forEach(venueImageRepo::delete);

            // Save new images
            for (MultipartFile imageFile : images) {
                try {
                    Venue_image image = new Venue_image();
                    image.setImage(imageFile.getBytes());
                    image.setVenue(updatedVenue);
                    venueImageRepo.save(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<Long> imageIds = venueImageRepo.findByVenueId(updatedVenue.getId())
                .stream()
                .map(Venue_image::getId)
                .collect(Collectors.toList());

        VenueResponse venueResponse = new VenueResponse(
                updatedVenue.getId(),
                updatedVenue.getVenueName(),
                updatedVenue.getVenuePackage(),
                updatedVenue.getVenueType(),
                updatedVenue.getCapacity(),
                updatedVenue.getDescription(),
                updatedVenue.getLocation(),
                imageIds
        );

        return new ResponseEntity<>(venueResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        Venue venue = venueRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + id));

        // Delete images associated with the venue
        venueImageRepo.findByVenueId(venue.getId()).forEach(venueImageRepo::delete);

        // Delete the venue
        venueRepo.delete(venue);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
