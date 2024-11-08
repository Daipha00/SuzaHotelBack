package com.reservation.HotelManagement.Controller;
import com.reservation.HotelManagement.Model.Hotel;
import com.reservation.HotelManagement.Model.HotelResponse;
import com.reservation.HotelManagement.Model.Image;
import com.reservation.HotelManagement.Model.ResourceNotFoundException;
import com.reservation.HotelManagement.Repository.HotelRepo;
import com.reservation.HotelManagement.Repository.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelRepo hotelRepository;

    @Autowired
    private ImageRepo imageRepository;

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam("images") MultipartFile[] images) {

        // Create a new hotel entity
        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelName);
        hotel.setLocation(location);

        // Save the hotel to the database first
        Hotel savedHotel = hotelRepository.save(hotel);

        // List to hold the saved image IDs
        List<Long> imageIds = new ArrayList<>();

        // Process the uploaded images and save them
        for (MultipartFile imageFile : images) {
            try {
                // Create a new Image entity
                Image image = new Image();
                image.setImage(imageFile.getBytes()); // Store the image data

                // Set the hotel reference to the saved hotel
                image.setHotel(savedHotel);

                // Save the image to the database
                Image savedImage = imageRepository.save(image);
                imageIds.add(savedImage.getId()); // Add the saved image ID to the list
            } catch (Exception e) {
                // Handle exceptions (e.g., log the error)
                e.printStackTrace();
            }
        }

        // Create a response object including the ID and image IDs
        HotelResponse hotelResponse = new HotelResponse(savedHotel.getId(), savedHotel.getHotelName(), savedHotel.getLocation(), imageIds);
        return new ResponseEntity<>(hotelResponse, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelResponse> hotelResponses = new ArrayList<>();

        for (Hotel hotel : hotels) {
            List<Long> imageIds = new ArrayList<>();

            // Fetch images for the current hotel
            List<Image> images = imageRepository.findByHotelId(hotel.getId());

            for (Image image : images) {
                imageIds.add(image.getId());
            }

            HotelResponse hotelResponse = new HotelResponse(hotel.getId(), hotel.getHotelName(), hotel.getLocation(), imageIds);
            hotelResponses.add(hotelResponse);
        }

        return new ResponseEntity<>(hotelResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        // Fetch the hotel by ID
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        // Fetch images for the hotel
        List<Image> images = imageRepository.findByHotelId(hotel.getId());

        // Collect image IDs
        List<Long> imageIds = new ArrayList<>();
        for (Image image : images) {
            imageIds.add(image.getId());
        }

        // Create the response object
        HotelResponse hotelResponse = new HotelResponse(hotel.getId(), hotel.getHotelName(), hotel.getLocation(), imageIds);

        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            hotel.setHotelName(hotelName);
            hotel.setLocation(location);

            // Update the hotel in the database
            Hotel updatedHotel = hotelRepository.save(hotel);

            // Declare the imageIds list here
            List<Long> imageIds = new ArrayList<>();

            // If images are provided, process them
            if (images != null) {
                for (MultipartFile imageFile : images) {
                    try {
                        // Create a new Image entity
                        Image image = new Image();
                        image.setImage(imageFile.getBytes()); // Store the image data

                        // Set the hotel reference to the updated hotel
                        image.setHotel(updatedHotel);

                        // Save the image to the database
                        Image savedImage = imageRepository.save(image);
                        imageIds.add(savedImage.getId()); // Add the saved image ID to the list
                    } catch (Exception e) {
                        // Handle exceptions (e.g., log the error)
                        e.printStackTrace();
                    }
                }
            }

            // Create a response object including the updated hotel details
            HotelResponse hotelResponse = new HotelResponse(updatedHotel.getId(), updatedHotel.getHotelName(), updatedHotel.getLocation(), imageIds);
            return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            hotelRepository.delete(hotelOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Hotel not found
        }
    }
}
