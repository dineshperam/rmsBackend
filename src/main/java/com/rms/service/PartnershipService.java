package com.rms.service;

import java.util.Calendar;
import java.util.Date;
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
    
    public Optional<Partnership> getPartnershipByArtistId(int artistId) {
    	return partnershipRepository.findMostRecentByArtistId(artistId);
    			
    }

    // Update an existing Partnership
    public Partnership updatePartnership(int id, Partnership partnership) {
        Partnership existing = getPartnershipById(id); // Use the method here
        existing.setArtistId(partnership.getArtistId());
        existing.setManagerId(partnership.getManagerId());
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
    
    

    // **Artist sends a partnership request**
       public Partnership sendRequest(int artistId, int managerId, Double percentage, int durationMonths, String comments) {
           if (durationMonths <= 0) {
               throw new RuntimeException("Duration must be greater than 0.");
           }

           if (percentage == null) {  // ✅ Ensure percentage is not null
               throw new RuntimeException("Percentage cannot be null.");
           }

           // Ensure unique request
           Optional<Partnership> existingRequest = partnershipRepository.findByArtistIdAndManagerIdAndStatus(artistId, managerId, "ACCEPTED");
           if (existingRequest.isPresent()) {
               throw new RuntimeException("Request already sent to this manager.");
           }

           Date startDate = new Date(); // ✅ Current timestamp
           Date endDate = calculateEndDate(startDate, durationMonths);

           Partnership partnership = new Partnership();
           partnership.setArtistId(artistId);
           partnership.setManagerId(managerId);
           partnership.setPercentage(percentage);
           partnership.setDurationMonths(durationMonths);
           partnership.setComments(comments);
           partnership.setStatus("PENDING");
           partnership.setStartDate(startDate);
           partnership.setEndDate(endDate);

           return partnershipRepository.save(partnership);
       }


       // **Manager retrieves all pending requests**
       public List<Partnership> getRequestsForManager(int managerId) {
           return partnershipRepository.findByManagerIdAndStatus(managerId, "PENDING");
       }

       // **Manager accepts or rejects the request**
       public Partnership respondToRequest(int partnershipId, String status) {
           Partnership partnership = partnershipRepository.findById(partnershipId)
                   .orElseThrow(() -> new RuntimeException("Request not found"));

           if ("Accepted".equalsIgnoreCase(status)) {
               partnership.setStatus("ACCEPTED");
               partnership.setEndDate(calculateEndDate(partnership.getStartDate(), partnership.getDurationMonths()));

               // Update artist's managerId
               UserDetails artist = userDetailsRepository.findById(partnership.getArtistId())
                       .orElseThrow(() -> new RuntimeException("Artist not found"));
               artist.setManagerId(partnership.getManagerId());
               userDetailsRepository.save(artist);
           } else if ("Rejected".equalsIgnoreCase(status)) {
               partnership.setStatus("INACTIVE");
           } else {
               throw new RuntimeException("Invalid status: " + status);
           }

           return partnershipRepository.save(partnership);
       }

       // **Calculate End Date**
       private Date calculateEndDate(Date startDate, int durationMonths) {
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(startDate);
           calendar.add(Calendar.MONTH, durationMonths);
           return calendar.getTime();
       }



}
