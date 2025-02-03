package com.reservation.HotelManagement.Repository;

import com.reservation.HotelManagement.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    // Custom method to find Role by roleName
    Optional<Role> findByRoleName(String roleName);
}
