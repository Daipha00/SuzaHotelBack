package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findByUserName(User user);
    User findByUserName(String userName);
//    Optional<User> findByUserName(String userName);
}
