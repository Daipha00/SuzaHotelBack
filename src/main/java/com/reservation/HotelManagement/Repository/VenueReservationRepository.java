package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Room_reservation;
import com.reservation.HotelManagement.Model.Venue_reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenueReservationRepository extends JpaRepository<Venue_reservation, Long> {
    @Query("SELECT r FROM Venue_reservation r WHERE r.venue.id = :venueId AND " +
            "((r.check_in <= :check_out AND r.check_out >= :check_in))")
    List<Venue_reservation> findOverlappingReservations(@Param("venueId") Long venueId,
                                                       @Param("check_in") LocalDate check_in,
                                                       @Param("check_out") LocalDate check_out);

    @Query("SELECT r FROM Venue_reservation r WHERE r.check_in = :check_in")
    List<Venue_reservation> findByCheckIn(@Param("check_in") LocalDate check_in);


}
