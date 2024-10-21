package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Reservation;
import com.reservation.HotelManagement.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.room.id = ?1")
    List<Reservation> getReservationByRoom(Long id);
}
