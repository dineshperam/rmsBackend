package com.rms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rms.model.Partnership;
import com.rms.service.PartnershipService;

@RestController
@RequestMapping("/partnerships")
public class PartnershipController {

    @Autowired
    private PartnershipService partnershipService;
   
    @PostMapping("/add")
    public ResponseEntity<Partnership> createPartnership(@RequestBody Partnership partnership) {
        Partnership createdPartnership = partnershipService.createPartnership(partnership);
        return new ResponseEntity<>(createdPartnership, HttpStatus.CREATED);
    }

    @GetMapping("/showAllPartners")
    public ResponseEntity<List<Partnership>> getAllPartnerships() {
        List<Partnership> partnerships = partnershipService.getAllPartnerships();
        return new ResponseEntity<>(partnerships, HttpStatus.OK);
    }

    @GetMapping("/showbyID/{id}")
    public ResponseEntity<Partnership> getPartnershipById(@PathVariable int id) {
        Partnership partnership = partnershipService.getPartnershipById(id);
        return new ResponseEntity<>(partnership, HttpStatus.OK);
    }

//    @PutMapping("/updatebyId/{id}")
//    public ResponseEntity<Partnership> updatePartnership(
//            @PathVariable int id, @RequestBody Partnership partnership) {
//        Partnership updatedPartnership = partnershipService.updatePartnership(id, partnership);
//        return new ResponseEntity<>(updatedPartnership, HttpStatus.OK);
//    }
    
    @PutMapping("/updatebyId/{id}")
    public ResponseEntity<Partnership> updatePartnership(
            @PathVariable int id, @RequestBody Partnership partnership) {
        Partnership updatedPartnership = partnershipService.updatePartnership(id, partnership);
        return new ResponseEntity<>(updatedPartnership, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deletePartnership(@PathVariable int id) {
        partnershipService.deletePartnership(id);
        return new ResponseEntity<>("Partnership deleted successfully!", HttpStatus.OK);
    }
    
 // **Artist sends a partnership request**
    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(@RequestBody Map<String, Object> requestMap) {
        try {
            System.out.println("Received Request Payload: " + requestMap); // Debugging log

            // Ensure values are not null before casting
            Integer artistId = requestMap.get("artistId") instanceof Number ? ((Number) requestMap.get("artistId")).intValue() : null;
            Integer managerId = requestMap.get("managerId") instanceof Number ? ((Number) requestMap.get("managerId")).intValue() : null;
            Double percentage = requestMap.get("percentage") instanceof Number ? ((Number) requestMap.get("percentage")).doubleValue() : null;
            Integer durationMonths = requestMap.get("durationMonths") instanceof Number ? ((Number) requestMap.get("durationMonths")).intValue() : null;
            String comments = (String) requestMap.getOrDefault("comments", "");

            // Validate required fields
            if (artistId == null || managerId == null || percentage == null || durationMonths == null) {
                return ResponseEntity.badRequest().body("Error: Missing required fields.");
            }

            if (percentage <= 0.0) {
                return ResponseEntity.badRequest().body("Error: Percentage must be greater than zero.");
            }

            Partnership partnership = partnershipService.sendRequest(artistId, managerId, percentage, durationMonths, comments);
            return ResponseEntity.ok(partnership);
        } catch (Exception e) {
            e.printStackTrace(); // Log the actual error
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }


    // **Manager retrieves all pending requests**
    @GetMapping("/requests/{managerId}")
    public ResponseEntity<List<Partnership>> getRequestsForManager(@PathVariable int managerId) {
        return ResponseEntity.ok(partnershipService.getRequestsForManager(managerId));
    }

   

}
