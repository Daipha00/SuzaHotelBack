package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Image;
import com.reservation.HotelManagement.Model.Room;
import com.reservation.HotelManagement.Model.Room_image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomImageRepo extends JpaRepository<Room_image, Long> {
    List<Room_image> findByRoomId(Long roomId);
}
