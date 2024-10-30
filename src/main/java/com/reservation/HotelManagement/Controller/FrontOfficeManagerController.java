package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.FrontOfficerManager;
import com.reservation.HotelManagement.Repository.FrontManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/frontManager")


@CrossOrigin(originPatterns = "*")


public class FrontOfficeManagerController {

    @Autowired
    private FrontManagerRepo frontOfficerManagerRepo;

    // Create or update a front officer manager
    @PostMapping
    public ResponseEntity<FrontOfficerManager> saveOrUpdateFrontOfficerManager(@RequestBody FrontOfficerManager frontOfficerManager) {
        FrontOfficerManager savedFrontOfficerManager = frontOfficerManagerRepo.save(frontOfficerManager);
        return new ResponseEntity<>(savedFrontOfficerManager, HttpStatus.CREATED);
    }

    // Retrieve all front officer managers
    @GetMapping
    public ResponseEntity<List<FrontOfficerManager>> getAllFrontOfficerManagers() {
        List<FrontOfficerManager> frontOfficerManagers = frontOfficerManagerRepo.findAll();
        return new ResponseEntity<>(frontOfficerManagers, HttpStatus.OK);
    }

    // Retrieve a front officer manager by ID
    @GetMapping("/{id}")
    public ResponseEntity<FrontOfficerManager> getFrontOfficerManagerById(@PathVariable Long id) {
        Optional<FrontOfficerManager> frontOfficerManager = frontOfficerManagerRepo.findById(id);
        return frontOfficerManager.map(f -> new ResponseEntity<>(f, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a front officer manager
    @PutMapping("/{id}")
    public ResponseEntity<FrontOfficerManager> updateFrontOfficerManager(@PathVariable Long id, @RequestBody FrontOfficerManager frontOfficerManager) {
        Optional<FrontOfficerManager> existingFrontOfficerManager = frontOfficerManagerRepo.findById(id);
        if (existingFrontOfficerManager.isPresent()) {
            frontOfficerManager.setId(id); // Ensure the correct ID is being updated
            FrontOfficerManager updatedFrontOfficerManager = frontOfficerManagerRepo.save(frontOfficerManager);
            return new ResponseEntity<>(updatedFrontOfficerManager, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a front officer manager by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrontOfficerManager(@PathVariable Long id) {
        Optional<FrontOfficerManager> frontOfficerManager = frontOfficerManagerRepo.findById(id);
        if (frontOfficerManager.isPresent()) {
            frontOfficerManagerRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
