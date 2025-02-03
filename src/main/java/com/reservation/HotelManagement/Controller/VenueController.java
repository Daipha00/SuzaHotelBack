package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.VenueImageRepo;
import com.reservation.HotelManagement.Repository.VenueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long imageId) {
        Optional<Venue_image> image = venueImageRepo.findById(imageId);
        if (image.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust the content type if needed
                    .body(image.get().getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @GetMapping("/{venueId}/image")
    public ResponseEntity<byte[]> getFirstImageForVenue(@PathVariable Long venueId) {
        // Get the hotel by ID (optional, for validation)
        Optional<Venue> venueOptional = venueRepo.findById(venueId);
        if (!venueOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
        }

        Venue venue = venueOptional.get();

        // Get the first image for the hotel
        List<Venue_image> images = venueImageRepo.findByVenueIdOrderByIdAsc(venueId); // Assuming you have an ordering mechanism

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
        }

        Venue_image firstImage = images.get(0); // Get the first image
        return ResponseEntity.ok()
                .contentType(getMediaType(firstImage)) // Dynamically set the content type
                .body(firstImage.getImage()); // Return the image bytes
    }
    private MediaType getMediaType(Venue_image image) {
        // Get the file extension or check the first few bytes of the image to determine the format
        String fileExtension = getFileExtension(image);

        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // Default for unknown image types
        }
    }

    // Helper method to get file extension (basic example)
    private String getFileExtension(Venue_image image) {
        // You can implement logic here to check the file format of the image byte[] if needed
        // For simplicity, let's assume the file type is known or inferred from the byte[]
        return "jpg"; // Placeholder for actual logic, you can enhance this
    }

    @GetMapping("/{venueId}/images")
    public ResponseEntity<List<byte[]>> getAllImagesForVenue(@PathVariable Long venueId) {
        // Get the hotel by ID (optional, for validation)
        Optional<Venue> venueOptional = venueRepo.findById(venueId);
        if (!venueOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
        }

        Venue venue = venueOptional.get();

        // Get all images for the hotel
        List<Venue_image> images = venueImageRepo.findByVenueIdOrderByIdAsc(venueId); // Fetch images by hotel ID

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
        }

        // Extract image byte[] from the list
        List<byte[]> imageBytes = images.stream()
                .map(Venue_image::getImage) // Assuming Image entity has a `getImage` method that returns the image byte array
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(imageBytes); // Return the list of images as response
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
