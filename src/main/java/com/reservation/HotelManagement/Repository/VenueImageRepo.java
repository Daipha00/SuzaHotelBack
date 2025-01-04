package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Image;
import com.reservation.HotelManagement.Model.Venue_image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueImageRepo extends JpaRepository<Venue_image, Long> {

    List<Venue_image> findByVenueId(Long venueId);

    List<Venue_image> findByVenueIdOrderByIdAsc(Long venueId);
}
