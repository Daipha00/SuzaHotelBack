package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.FrontOfficeStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontStaffRepo extends JpaRepository<FrontOfficeStaff, Long> {
}
