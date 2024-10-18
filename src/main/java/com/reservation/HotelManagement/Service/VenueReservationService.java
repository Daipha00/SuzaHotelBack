package com.reservation.HotelManagement.Service;

import com.reservation.HotelManagement.Model.Venue_reservation;
import com.reservation.HotelManagement.Repository.VenueReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueReservationService {

    @Autowired
    private VenueReservationRepository venueReservationRepository;

    public Venue_reservation createVenueReservation(Venue_reservation venueReservation) {
        return venueReservationRepository.save(venueReservation);
    }

    public List<Venue_reservation> getAllVenueReservations() {
        return venueReservationRepository.findAll();
    }

    public ResponseEntity<Venue_reservation> getVenueReservationById(Long id) {
        Venue_reservation venueReservation = venueReservationRepository.findById(id).orElse(null);
        return ResponseEntity.ok(venueReservation);
    }

    public void deleteVenueReservation(Long id) {
        venueReservationRepository.deleteById(id);
    }

    public ResponseEntity<Venue_reservation> updateVenueReservation(Long id, Venue_reservation updatedVenueReservation) {
        Venue_reservation venueReservation = venueReservationRepository.findById(id).orElse(null);
        if (venueReservation != null) {
            updatedVenueReservation.setId(id);
            venueReservationRepository.save(updatedVenueReservation);
        }
        return ResponseEntity.ok(updatedVenueReservation);
    }
}
