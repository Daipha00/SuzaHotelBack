package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.HotelManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelManagerRepo extends JpaRepository<HotelManager, Long> {

}
