package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Room_reservation;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
public interface RoomReservationRepository extends JpaRepository<Room_reservation, Long> {
}
