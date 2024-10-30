package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.HotelManager;
import com.reservation.HotelManagement.Repository.HotelManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotelManager")
@CrossOrigin(originPatterns = "*")

public class HotelManagerController {

    @Autowired
    private HotelManagerRepo hotelManagerRepo;

    // Create or update a hotel manager
    @PostMapping
    public ResponseEntity<HotelManager> saveHotelManager(@RequestBody HotelManager hotelManager) {
        HotelManager savedHotelManager = hotelManagerRepo.save(hotelManager);
        return new ResponseEntity<>(savedHotelManager, HttpStatus.CREATED);
    }

    // Retrieve all hotel managers
    @GetMapping
    public ResponseEntity<List<HotelManager>> getAllHotelManagers() {
        List<HotelManager> hotelManagers = hotelManagerRepo.findAll();
        return new ResponseEntity<>(hotelManagers, HttpStatus.OK);
    }

    // Retrieve a hotel manager by ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelManager> getHotelManagerById(@PathVariable Long id) {
        Optional<HotelManager> hotelManager = hotelManagerRepo.findById(id);
        return hotelManager.map(manager -> new ResponseEntity<>(manager, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a hotel manager
    @PutMapping("/{id}")
    public ResponseEntity<HotelManager> updateHotelManager(@PathVariable Long id, @RequestBody HotelManager hotelManager) {
        Optional<HotelManager> existingHotelManager = hotelManagerRepo.findById(id);
        if (existingHotelManager.isPresent()) {
            hotelManager.setId(id); // Set the ID to ensure we are updating the correct record
            HotelManager updatedManager = hotelManagerRepo.save(hotelManager);
            return new ResponseEntity<>(updatedManager, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a hotel manager by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelManager(@PathVariable Long id) {
        Optional<HotelManager> hotelManager = hotelManagerRepo.findById(id);
        if (hotelManager.isPresent()) {
            hotelManagerRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
