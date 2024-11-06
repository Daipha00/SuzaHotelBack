package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Room;
import com.reservation.HotelManagement.Model.Hotel;
import com.reservation.HotelManagement.Repository.RoomRepo;
import com.reservation.HotelManagement.Repository.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private HotelRepo hotelRepo;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<Room> createNewRoom(
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("hotelId") Long hotelId) {

        // Fetch the Hotel entity using the hotelId from the URL parameter
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        try {
            // Convert the image to byte array
            byte[] imageBytes = image.getBytes();

            // Create a new Room entity
            Room room = new Room();
            room.setRoomType(roomType);
            room.setPax(pax);
            room.setDescription(description);
            room.setImage(imageBytes); // Set the image as a byte array
            room.setHotel(hotel); // Set the hotel object directly

            // Save the room to the database
            Room savedRoom = roomRepo.save(room);

            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
