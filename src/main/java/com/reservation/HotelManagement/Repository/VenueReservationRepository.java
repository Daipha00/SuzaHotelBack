package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Venue_reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueReservationRepository extends JpaRepository<Venue_reservation, Long> {
}
