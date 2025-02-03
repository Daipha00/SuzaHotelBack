package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
//import com.reservation.HotelManagement.Model.ReservationStatus;
import com.reservation.HotelManagement.Model.Room;
import com.reservation.HotelManagement.Model.Room_reservation;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.RoomRepo;
import com.reservation.HotelManagement.Repository.RoomReservationRepository;
import com.reservation.HotelManagement.Service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room_reservation")
@CrossOrigin(originPatterns = "*")

public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;
    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private RoomReservationRepository roomReservationRepository;



    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createNewRoomReservation(@RequestBody Room_reservation reservation,
                                                           @RequestParam Long clientId,
                                                           @RequestParam Long roomId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Check for existing reservations
        List<Room_reservation> existingReservations = roomReservationRepository.findOverlappingReservations(
                roomId, reservation.getCheck_in(), reservation.getCheck_out());

        if (!existingReservations.isEmpty()) {
            return ResponseEntity.badRequest().body("The room is already booked between these dates.");
        }

        reservation.setClient(client); // Set the client for the reservation
        reservation.setRoom(room); // Set the room for the reservation
        reservation.setStatus("Pending"); // Set the default status to Pending

        roomReservationRepository.save(reservation);
        return ResponseEntity.ok("Room reservation created successfully.");
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

    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelRoomReservation(@PathVariable Long reservationId) {
        Room_reservation reservation = roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if ("Confirmed".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation cannot be cancelled as it has already been confirmed.");
        }

        if ("Cancelled".equalsIgnoreCase(reservation.getStatus())) {
            return ResponseEntity.badRequest().body("Reservation is already cancelled.");
        }

        reservation.setStatus("Cancelled"); // Update the status to Cancelled
        roomReservationRepository.save(reservation);
        return ResponseEntity.ok("Reservation cancelled successfully.");
    }


    }

