package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Hotel;
import com.reservation.HotelManagement.Repository.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelRepo hotelRepo;

    // Endpoint to add a new hotel with images
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Hotel> addHotel(
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam("images") List<MultipartFile> images) {

        try {
            // Convert MultipartFiles to byte arrays and add them to the list
            List<byte[]> imageBytesList = new ArrayList<>();
            for (MultipartFile image : images) {
                imageBytesList.add(image.getBytes());
            }

            // Create a new Hotel entity
            Hotel hotel = new Hotel();
            hotel.setHotelName(hotelName);
            hotel.setLocation(location);
            hotel.setImages(imageBytesList);

            // Save the hotel to the database
            Hotel savedHotel = hotelRepo.save(hotel);

            return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelRepo.findAll();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable("id") Long id) {
        Optional<Hotel> hotel = hotelRepo.findByIdWithImages(id);
        if (hotel.isPresent()) {
            return new ResponseEntity<>(hotel.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteHotelById(@PathVariable("id") Long id) {
        try {
            if (hotelRepo.existsById(id)) {
                hotelRepo.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Successfully deleted
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Hotel not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Error occurred
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(
            @PathVariable("id") Long id,
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam("images") List<MultipartFile> images) {

        Optional<Hotel> existingHotelOpt = hotelRepo.findById(id);
        if (!existingHotelOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Hotel not found
        }

        try {
            Hotel existingHotel = existingHotelOpt.get();

            // Update fields
            existingHotel.setHotelName(hotelName);
            existingHotel.setLocation(location);

            // Convert MultipartFiles to byte arrays and update images
            List<byte[]> imageBytesList = new ArrayList<>();
            for (MultipartFile image : images) {
                imageBytesList.add(image.getBytes());
            }
            existingHotel.setImages(imageBytesList);

            // Save the updated hotel to the database
            Hotel updatedHotel = hotelRepo.save(existingHotel);

            return new ResponseEntity<>(updatedHotel, HttpStatus.OK);  // Successfully updated
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Error occurred
        }
    }

}
