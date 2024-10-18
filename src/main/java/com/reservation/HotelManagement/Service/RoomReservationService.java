package com.reservation.HotelManagement.Service;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Room_reservation;
import com.reservation.HotelManagement.Repository.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomReservationService {
    @Autowired
    private RoomReservationRepository roomReservationRepository;

    public Room_reservation createRoomReservation(Room_reservation roomReservation, Client client) {
        roomReservation.setClient(client);
        return roomReservationRepository.save(roomReservation);
    }

    public List<Room_reservation> getAllRoomReservations() {
        return roomReservationRepository.findAll();
    }

    public ResponseEntity<Room_reservation> getRoomReservationById(Long id) {
        Room_reservation roomReservation = roomReservationRepository.findById(id).orElse(null);
        return ResponseEntity.ok(roomReservation);
    }

    public void deleteRoomReservation(Long id) {
        roomReservationRepository.deleteById(id);
    }

    public ResponseEntity<Room_reservation> updateRoomReservation(Long id, Room_reservation updatedRoomReservation) {
        Room_reservation roomReservation = roomReservationRepository.findById(id).orElse(null);
        if (roomReservation != null) {
            updatedRoomReservation.setId(id);
            roomReservationRepository.save(updatedRoomReservation);
        }
        return ResponseEntity.ok(updatedRoomReservation);
    }
}
