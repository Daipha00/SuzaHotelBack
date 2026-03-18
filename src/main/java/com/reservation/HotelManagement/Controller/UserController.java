package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.LoginRequest;
import com.reservation.HotelManagement.Model.User;
import com.reservation.HotelManagement.Repository.ClientRepo;
import com.reservation.HotelManagement.Repository.UserRepo;
import com.reservation.HotelManagement.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/client")
    public ResponseEntity<String> registerNewClient(@RequestBody Client client) {

        // 1. Cheki email kwenye DB
        boolean emailExistsClient = clientRepo.findByEmail(client.getEmail()).isPresent();
        boolean emailExistsUser = userRepo.findByEmail(client.getEmail()).isPresent();

        if (emailExistsClient || emailExistsUser) {
            // 2. STOP! Hapatoshe user yoyote DB
            return ResponseEntity
                    .badRequest()
                    .body("Email already exists.");
        }

        // 3. Save data tu ikiwa email haipo
        User newUser = userService.registerNewClient(client);

        return ResponseEntity.ok("Registration successful!");
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
