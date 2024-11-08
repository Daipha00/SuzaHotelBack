package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

//    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.images WHERE h.id = :id")
//    Optional<Hotel> findByIdWithImages(Long id);
}
