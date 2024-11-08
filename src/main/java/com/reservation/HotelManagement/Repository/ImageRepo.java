package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    List<Image> findByHotelId(Long hotelId);
}
