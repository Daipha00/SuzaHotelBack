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

        Role clientRole = new Role();
        clientRole.setRoleName("Client");
        clientRole.setRoleDescription("Role for client only");
        roleRepo.save(clientRole);

        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUserPassword("admin123");
        adminUser.setUserFirstName("John");
        adminUser.setUserLastName("Musa");
        adminUser.setEmail("msagaladaines@gmail.com");
        adminUser.setAddress("Jumbi");
        adminUser.setPhoneNumber("0710287645");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepo.save(adminUser);

    }

    @Transactional
    public Client registerNewClient(Client client) {
        Role role = roleRepo.findByRoleName("Client")
                .orElseThrow(() -> new IllegalArgumentException("Role 'Client' not found"));
        Set<Role> clientRoles = new HashSet<>();
        clientRoles.add(role);
        client.setRole(clientRoles);

        return clientRepo.save(client); // Ensure clientRepo is used
    }

    public User loginUser(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (!user.getUserPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        return user;
    }

}
