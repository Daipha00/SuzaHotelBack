package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Room_reservation;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.RoomReservationRepository;
import com.reservation.HotelManagement.Service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room_reservation")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;
    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private RoomReservationRepository roomReservationRepository;


    @PostMapping
    @ResponseBody
    public Room_reservation createNewRoomReservation(@RequestBody Room_reservation roomReservation, @RequestParam Long clientId) {
        Client client = clientRepo.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        roomReservation.setClient(client); // Set the client for the reservation
        return roomReservationRepository.save(roomReservation);
    }


    // Get all room reservations
    @GetMapping
    public List<Room_reservation> getAllRoomReservations() {
        return roomReservationService.getAllRoomReservations();
    }

    // Get room reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room_reservation> getRoomReservationById(@PathVariable Long id) {
        return roomReservationService.getRoomReservationById(id);
    }

    // Delete room reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomReservation(@PathVariable Long id) {
        roomReservationService.deleteRoomReservation(id);
        return ResponseEntity.noContent().build();
    }

    // Update room reservation
    @PutMapping("/{id}")
    public ResponseEntity<Room_reservation> updateRoomReservation(@PathVariable Long id, @RequestBody Room_reservation updatedRoomReservation) {
        return roomReservationService.updateRoomReservation(id, updatedRoomReservation);
    }
    }

