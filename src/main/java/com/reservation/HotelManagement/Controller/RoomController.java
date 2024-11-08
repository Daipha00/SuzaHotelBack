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
import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomRepo.findAll(); // Fetch all rooms

        // Return the list of rooms with their associated hotel information
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomRepo.findById(id);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("hotelId") Long hotelId) {

        Optional<Room> optionalRoom = roomRepo.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Room room = optionalRoom.get();
        room.setRoomType(roomType);
        room.setPax(pax);
        room.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                room.setImage(imageBytes);
            } catch (IOException e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        room.setHotel(hotel);

        Room updatedRoom = roomRepo.save(room);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (!roomRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        roomRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
