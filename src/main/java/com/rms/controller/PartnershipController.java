package com.rms.controller;

import java.util.List;

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
    
    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(@RequestParam int artistId, @RequestParam int managerId) {
        try {
            Partnership partnership = partnershipService.sendRequest(artistId, managerId);
            return ResponseEntity.ok(partnership);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // Return a meaningful error message
        }
    }

    @GetMapping("/requests/{managerId}")
    public ResponseEntity<List<Partnership>> getRequestsForManager(@PathVariable int managerId) {
        return ResponseEntity.ok(partnershipService.getRequestsForManager(managerId));
    }

    @PutMapping("/respond/{partnershipId}")
    public ResponseEntity<Partnership> respondToRequest(@PathVariable int partnershipId, @RequestParam String status) {
        return ResponseEntity.ok(partnershipService.respondToRequest(partnershipId, status));
    }

}
