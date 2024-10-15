package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.FrontOfficerManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontManagerRepo extends JpaRepository<FrontOfficerManager, Long> {
}
