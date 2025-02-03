package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.LoginRequest;
import com.reservation.HotelManagement.Model.User;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.UserRepo;
import com.reservation.HotelManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController

@CrossOrigin(originPatterns = "*")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClientRepo clientRepo;

    @PostConstruct
    private void initUserRoles(){
        userService.initUserRoles();
    }


    @PostMapping({"/client"})
    public User registerNewClient(@RequestBody Client client) {
        if (clientRepo.findByEmail(client.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }
        return userService.registerNewClient(client);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getUserPassword());
            return ResponseEntity.ok(user); // Return user data if successful
        } catch (IllegalArgumentException e) {
            // Return error if user not found or incorrect password
            return ResponseEntity.status(401).body(null); // 401 Unauthorized
        }
    }

    @GetMapping({"/forAdmin"})
    public String forAdmin(){
        return "This URL is only accessible by admin";
    }

    @GetMapping({"/forUser"})
    public String forUser(){
        return "This URL is only accessible by user";
    }

    @GetMapping("/viewOwnDetails/{id}")
    public Optional<User> viewOwnDetails(@PathVariable Long id) {
        return userRepo.findById(id);
    }


}
