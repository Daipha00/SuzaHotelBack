package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.JwtRequest;
import com.reservation.HotelManagement.Model.JwtResponse;
import com.reservation.HotelManagement.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v3/")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }
}
