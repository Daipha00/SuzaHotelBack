package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.RoomImageRepo;
import com.reservation.HotelManagement.Repository.RoomRepo;
import com.reservation.HotelManagement.Repository.HotelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private RoomImageRepo roomImageRepo;
    @Autowired
    private HotelRepo hotelRepo;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("description") String description,
            @RequestParam("images") MultipartFile[] images) {

        // Validate and fetch the associated hotel
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Create and save the room
        Room room = new Room();
        room.setRoomType(roomType);
        room.setPax(pax);
        room.setDescription(description);
        room.setHotel(hotel);
        Room savedRoom = roomRepo.save(room);

        // Save images and associate them with the room
        List<Long> imageIds = new ArrayList<>();
        for (MultipartFile imageFile : images) {
            try {
                Room_image image = new Room_image();
                image.setImage(imageFile.getBytes());
                image.setRoom(savedRoom);
                Room_image savedImage = roomImageRepo.save(image);
                imageIds.add(savedImage.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RoomResponse roomResponse = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getPax(), savedRoom.getDescription(), imageIds);
        return new ResponseEntity<>(roomResponse, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<Room> rooms = roomRepo.findAll();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : rooms) {
            List<Long> imageIds = new ArrayList<>();
            List<Room_image> images = roomImageRepo.findByRoomId(room.getId());
            for (Room_image image : images) {
                imageIds.add(image.getId());
            }
            RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(),
                    room.getPax(), room.getDescription(), imageIds);
            roomResponses.add(roomResponse);
        }

        return new ResponseEntity<>(roomResponses, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Fetch images associated with the room
        List<Long> imageIds = new ArrayList<>();
        List<Room_image> images = roomImageRepo.findByRoomId(room.getId());
        for (Room_image image : images) {
            imageIds.add(image.getId());
        }

        // Create a response object
        RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(),
                room.getPax(), room.getDescription(), imageIds);

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @RequestParam("roomType") String roomType,
            @RequestParam("pax") int pax,
            @RequestParam("description") String description,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Update room details
        room.setRoomType(roomType);
        room.setPax(pax);
        room.setDescription(description);
        Room updatedRoom = roomRepo.save(room);

        // Process and save new images, if provided
        List<Long> imageIds = new ArrayList<>();
        if (images != null) {
            for (MultipartFile imageFile : images) {
                try {
                    Room_image image = new Room_image();
                    image.setImage(imageFile.getBytes());
                    image.setRoom(updatedRoom);
                    Room_image savedImage = roomImageRepo.save(image);
                    imageIds.add(savedImage.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Fetch existing image IDs if no new images are provided
            List<Room_image> existingImages = roomImageRepo.findByRoomId(id);
            for (Room_image image : existingImages) {
                imageIds.add(image.getId());
            }
        }

        // Create a response object
        RoomResponse roomResponse = new RoomResponse(updatedRoom.getId(), updatedRoom.getRoomType(),
                updatedRoom.getPax(), updatedRoom.getDescription(), imageIds);

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Delete associated images first
        List<Room_image> images = roomImageRepo.findByRoomId(id);
        roomImageRepo.deleteAll(images);

        // Delete the room
        roomRepo.delete(room);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
