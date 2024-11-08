package com.reservation.HotelManagement.Service;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClientRepo clientRepo;


    public User createNewUser(User user){
        return userRepo.save(user);
    }

    @Transactional
    public void initUserRoles(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Role for admin only");
        roleRepo.save(adminRole);

//        Role userRole = new Role();
//        userRole.setRoleName("User");
//        userRole.setRoleDescription("Role for user only");
//        roleRepo.save(userRole);

        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUserName("haji");
        adminUser.setUserPassword("admin123");
        adminUser.setUserFirstName("Rahel");
        adminUser.setUserLastName("Ombeni");
        adminUser.setEmail("msagaladaines@gmail.com");
        adminUser.setAddress("Jumbi");
        adminUser.setPhoneNumber("0710287645");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepo.save(adminUser);

    }


//    public String getEncodedPassword (String password){
//        return passwordEncoder.encode(password);
//    }
}
