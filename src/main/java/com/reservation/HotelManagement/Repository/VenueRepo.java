package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepo extends JpaRepository<Venue, Long> {
}
