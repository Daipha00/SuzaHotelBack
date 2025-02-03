package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Room_reservation;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<Room_reservation, Long> {

        @Query("SELECT r FROM Room_reservation r WHERE r.room.id = :roomId AND " +
                "((r.check_in <= :check_out AND r.check_out >= :check_in))")
        List<Room_reservation> findOverlappingReservations(@Param("roomId") Long roomId,
                                                           @Param("check_in") LocalDate check_in,
                                                           @Param("check_out") LocalDate check_out);

    List<Room_reservation> findByClientId(Long clientId);
    }

