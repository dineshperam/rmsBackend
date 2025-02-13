package com.rms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Partnership;
import com.rms.model.UserDetails;
import com.rms.repository.PartnershipRepository;
import com.rms.repository.UserDetailsRepository;

@Service
public class PartnershipService {

    @Autowired
    private PartnershipRepository partnershipRepository;
    
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    // Create a new Partnership
    public Partnership createPartnership(Partnership partnership) {
        return partnershipRepository.save(partnership);
    }

    // Get all Partnerships
    public List<Partnership> getAllPartnerships() {
        return partnershipRepository.findAll();
    }

    // Get a Partnership by ID
    public Partnership getPartnershipById(int id) {
        return partnershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partnership not found with ID: " + id));
    }

    // Update an existing Partnership
    public Partnership updatePartnership(int id, Partnership partnership) {
        Partnership existing = getPartnershipById(id); // Use the method here
        existing.setArtistid(partnership.getArtistid());
        existing.setManagerid(partnership.getManagerid());
        existing.setStatus(partnership.getStatus());
        return partnershipRepository.save(existing);
    }
    // Delete a Partnership by ID
    public void deletePartnership(int id) {
        if (!partnershipRepository.existsById(id)) {
            throw new RuntimeException("Partnership not found with ID: " + id);
        }
        partnershipRepository.deleteById(id);
    }
    
    public Partnership sendRequest(int artistId, int managerId) {
        Optional<Partnership> existingRequest = partnershipRepository.findByArtistidAndManagerid(artistId, managerId);

        if (existingRequest.isPresent()) {
            throw new RuntimeException("Request already sent to this manager.");
        }

        Partnership partnership = new Partnership();
        partnership.setArtistid(artistId);
        partnership.setManagerid(managerId);
        partnership.setStatus("Pending");

        return partnershipRepository.save(partnership);
    }


    public List<Partnership> getRequestsForManager(int managerId) {
        return partnershipRepository.findByManageridAndStatus(managerId, "Pending");
    }

    public Partnership respondToRequest(int partnershipId, String status) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        partnership.setStatus(status);
        partnershipRepository.save(partnership);

        if ("Accepted".equalsIgnoreCase(status)) {
            // Update artist's manager_id
            UserDetails artist = userDetailsRepository.findById(partnership.getArtistid())
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            artist.setManagerId(partnership.getManagerid());
            userDetailsRepository.save(artist);
        }

        return partnership;
    }

}
