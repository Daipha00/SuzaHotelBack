package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Room;
import com.reservation.HotelManagement.Repository.RoomRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "http://localhost:4200")

public class RoomController {

    @Autowired
    private RoomRepo roomRepo;  // Assuming you have a TrialRepository

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Room> createNewRoom(
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("price") Double price,
            @RequestParam("availabilityStatus") String availabilityStatus,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile) throws IOException {

        // Create a new Trial instance and set fields
        Room room = new Room();
        room.setRoomType(roomType);
        room.setPax(pax);
        room.setPrice(price);
        room.setAvailabilityStatus(availabilityStatus);
        room.setDescription(description);

        // Set the image as a byte array
        room.setImage(imageFile.getBytes());

        // Save the trial entity to the database
        Room savedRoom = roomRepo.save(room);

        // Return a response entity with the saved trial
        return ResponseEntity.ok(savedRoom);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRoom() {
        List<Room> rooms = roomRepo.findAll();
        return ResponseEntity.ok(rooms);
    }

    // GET method to retrieve a single trial by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("price") Double price,
            @RequestParam("availabilityStatus") String availabilityStatus,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        // Find the trial by ID
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (!existingRoomOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Update the trial's data
        Room existingRoom = existingRoomOpt.get();
        existingRoom.setRoomType(roomType);
        existingRoom.setPax(pax);
        existingRoom.setPrice(price);
        existingRoom.setAvailabilityStatus(availabilityStatus);
        existingRoom.setDescription(description);

        // If a new image is provided, update the image
        if (imageFile != null && !imageFile.isEmpty()) {
            existingRoom.setImage(imageFile.getBytes());
        }

        // Save the updated trial
        Room updatedRoom = roomRepo.save(existingRoom);
        return ResponseEntity.ok(updatedRoom);
    }

    // DELETE method to delete a trial by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrial(@PathVariable Long id) {
        Optional<Room> existingRoomOpt = roomRepo.findById(id);
        if (!existingRoomOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        roomRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
