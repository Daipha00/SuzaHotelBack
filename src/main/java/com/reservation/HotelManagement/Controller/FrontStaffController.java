package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.FrontOfficeStaff;
import com.reservation.HotelManagement.Repository.FrontStaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/frontStaff")
public class FrontStaffController {

    @Autowired
    private FrontStaffRepo frontOfficeStaffRepo;

    // Create or update a front office staff
    @PostMapping
    public ResponseEntity<FrontOfficeStaff> saveOrUpdateFrontOfficeStaff(@RequestBody FrontOfficeStaff frontOfficeStaff) {
        FrontOfficeStaff savedFrontOfficeStaff = frontOfficeStaffRepo.save(frontOfficeStaff);
        return new ResponseEntity<>(savedFrontOfficeStaff, HttpStatus.CREATED);
    }

    // Retrieve all front office staff
    @GetMapping
    public ResponseEntity<List<FrontOfficeStaff>> getAllFrontOfficeStaff() {
        List<FrontOfficeStaff> frontOfficeStaffList = frontOfficeStaffRepo.findAll();
        return new ResponseEntity<>(frontOfficeStaffList, HttpStatus.OK);
    }

    // Retrieve a front office staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<FrontOfficeStaff> getFrontOfficeStaffById(@PathVariable Long id) {
        Optional<FrontOfficeStaff> frontOfficeStaff = frontOfficeStaffRepo.findById(id);
        return frontOfficeStaff.map(f -> new ResponseEntity<>(f, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a front office staff
    @PutMapping("/{id}")
    public ResponseEntity<FrontOfficeStaff> updateFrontOfficeStaff(@PathVariable Long id, @RequestBody FrontOfficeStaff frontOfficeStaff) {
        Optional<FrontOfficeStaff> existingFrontOfficeStaff = frontOfficeStaffRepo.findById(id);
        if (existingFrontOfficeStaff.isPresent()) {
            frontOfficeStaff.setId(id); // Ensure the correct ID is being updated
            FrontOfficeStaff updatedFrontOfficeStaff = frontOfficeStaffRepo.save(frontOfficeStaff);
            return new ResponseEntity<>(updatedFrontOfficeStaff, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a front office staff by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrontOfficeStaff(@PathVariable Long id) {
        Optional<FrontOfficeStaff> frontOfficeStaff = frontOfficeStaffRepo.findById(id);
        if (frontOfficeStaff.isPresent()) {
            frontOfficeStaffRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
