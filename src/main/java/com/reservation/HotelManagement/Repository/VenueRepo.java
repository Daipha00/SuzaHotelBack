package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Reservation;
import com.reservation.HotelManagement.Model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepo extends JpaRepository<Venue, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.venue.id = ?1")
    List<Reservation> getReservationByVenue(Long id);
}
