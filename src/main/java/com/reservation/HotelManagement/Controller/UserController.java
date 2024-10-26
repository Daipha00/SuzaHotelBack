package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.FrontOfficerManager;
import com.reservation.HotelManagement.Model.HotelManager;
import com.reservation.HotelManagement.Model.User;
import com.reservation.HotelManagement.Repository.UserRepo;
import com.reservation.HotelManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v3/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PostConstruct
    private void initUserRoles(){
        userService.initUserRoles();
    }


//    @PostMapping({"/registerNewUser"})
//    public User registerNewUser(@RequestBody User user){
//        return userService.registerNewUser(user);
//    }

    @PostMapping({"/registerNewHotelManager"})
    public User registerNewHotelManager(@RequestBody HotelManager hotelManager){
        return userService.registerNewHotelManager(hotelManager);
    }

    @PostMapping({"/registerNewFrontManager"})
    public User registerNewFrontManager(@RequestBody FrontOfficerManager frontOfficerManager){
        return userService.registerNewFrontManager(frontOfficerManager);
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
