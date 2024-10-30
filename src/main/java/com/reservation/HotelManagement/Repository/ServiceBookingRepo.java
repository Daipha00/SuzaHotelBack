package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.ServiceBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBookingRepo extends JpaRepository<ServiceBooking, Long> {
}
