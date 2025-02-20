package com.rms.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rms.model.Partnership;
import com.rms.service.PartnershipService;
import com.rms.service.PdfService;

@RestController
@RequestMapping("/partnerships")
@CrossOrigin(origins = "*")
public class PartnershipController {

    @Autowired
    private PartnershipService partnershipService;
    
    @Autowired
    private PdfService pdfService;
   
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


    // Get partnership details by ID
    @GetMapping("/showbyID/{id}")
    public ResponseEntity<Partnership> getPartnershipById(@PathVariable int id) {
        return ResponseEntity.ok(partnershipService.getPartnershipById(id));
    }
    
    @GetMapping("/latest/{artistId}")
    public ResponseEntity<Partnership> getLatestPartnershipByArtistId(@PathVariable int artistId) {
        Optional<Partnership> partnership = partnershipService.getPartnershipByArtistId(artistId);
        
        return partnership.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/updatebyId/{id}")
    public ResponseEntity<Partnership> updatePartnership(
            @PathVariable int id, @RequestBody Partnership partnership) {
        Partnership updatedPartnership = partnershipService.updatePartnership(id, partnership);
        return new ResponseEntity<>(updatedPartnership, HttpStatus.OK);
    }

    // Delete a partnership (Admin-level operation)
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deletePartnership(@PathVariable int id) {
        partnershipService.deletePartnership(id);
        return ResponseEntity.ok("Partnership deleted successfully!");
    }
    
    @GetMapping("/export-pdf-partner/{artistId}")
    public ResponseEntity<byte[]> exportPartnershipsToPdf(@PathVariable int artistId) {
        Optional<Partnership> partnerships =partnershipService.getPartnershipByArtistId(artistId); // Fetch all partnerships
        byte[] pdfBytes =pdfService.generatePartnershipPdf(partnerships);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=partnerships.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
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

    // **Manager accepts or rejects the request**
    @PutMapping("/respond/{partnershipId}")
    public ResponseEntity<?> respondToRequest(@PathVariable int partnershipId, @RequestParam String status) {
        try {
            Partnership updatedPartnership = partnershipService.respondToRequest(partnershipId, status);
            if ("Accepted".equalsIgnoreCase(status)) {
                return ResponseEntity.ok("Partnership request accepted successfully!");
            } else if ("Rejected".equalsIgnoreCase(status)) {
                return ResponseEntity.ok("Partnership request rejected.");
            } else {
                return ResponseEntity.badRequest().body("Invalid status.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/pending/{artistId}")
    public ResponseEntity<Partnership> getPendingPartnershipRequest(@PathVariable int artistId) {
        Optional<Partnership> pendingRequest = partnershipService.getPendingRequestForArtist(artistId);
        return pendingRequest.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
