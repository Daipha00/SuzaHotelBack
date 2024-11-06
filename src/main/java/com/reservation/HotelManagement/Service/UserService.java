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

    @Autowired
    private FrontManagerRepo frontManagerRepo;

    @Autowired
    private HotelManagerRepo hotelManagerRepo;



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

        Role hotelManagerRole = new Role();
        hotelManagerRole.setRoleName("Hotel Manager");
        hotelManagerRole.setRoleDescription("Role for Hotel Manager only");
        roleRepo.save(hotelManagerRole);


        Role officerRole = new Role();
        officerRole.setRoleName("IT Officer");
        officerRole.setRoleDescription("Role for IT officer only");
        roleRepo.save(officerRole);

        Role frontManager = new Role();
        frontManager.setRoleName("Front office manager");
        frontManager.setRoleDescription("Role for Front office manager only");
        roleRepo.save(frontManager);

        Role frontStaff = new Role();
        frontStaff.setRoleName("Front office staff");
        frontStaff.setRoleDescription("Role for Front office staff only");
        roleRepo.save(frontStaff);

        Role client = new Role();
        client.setRoleName("Front office manager");
        client.setRoleDescription("Role for Front Office Manager only");
        roleRepo.save(client);

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



    public User registerNewHotelManager (HotelManager hotelManager){
        Role role = roleRepo.findById("Hotel Manager").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        hotelManager.setRole(userRoles);
        hotelManager.setUserPassword(hotelManager.getUserPassword());

        return hotelManagerRepo.save(hotelManager);
    }

//    public Client registerNewClient (Client client){
//          Role role = roleRepo.findById("Client").get();
//          Set<Role> userRoles = new HashSet<>();
//          userRoles.add(role);
//          client.setRole(userRoles);
//          client.setUserPassword(client.getUserPassword());
//
//          return clientRepo.save(client);
//    }

    public User registerNewFrontManager(FrontOfficerManager frontOfficerManager){
        Role role = roleRepo.findById("Front Office Manager").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        frontOfficerManager.setRole(userRoles);
        frontOfficerManager.setUserPassword(frontOfficerManager.getUserPassword());

        return frontManagerRepo.save(frontOfficerManager);
    }


//    public String getEncodedPassword (String password){
//        return passwordEncoder.encode(password);
//    }
}
