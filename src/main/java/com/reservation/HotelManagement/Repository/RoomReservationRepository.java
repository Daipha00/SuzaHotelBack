package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Room_reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomReservationRepository extends JpaRepository<Room_reservation,Long> {
}
