package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.client.id = ?1")
    List<Reservation> getReservationByClient(Long id);
}
