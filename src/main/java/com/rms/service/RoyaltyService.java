package com.rms.service;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.rms.model.Royalty;
import com.rms.model.Streams;
import com.rms.model.Transactions;
import com.rms.model.UserDetails;
import com.rms.repository.RoyaltyRepository;
import com.rms.repository.StreamsRepository;
import com.rms.repository.UserDetailsRepository;

import jakarta.transaction.Transactional;
 
 
@Service
public class RoyaltyService {
	@Autowired
	private RoyaltyRepository royaltyRepository;
	
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private StreamsRepository streamsRepository;
	
	@Autowired
	private EmailService emailService;
	
	public List<Royalty> showRoyalty(){
		return royaltyRepository.findAll();
	}
	public Royalty searchRoyaltyById(int royaltyId) {
		return royaltyRepository.findById(royaltyId).get();
	}
	public void addRoyalty(Royalty royalty) {
		royaltyRepository.save(royalty);
	}
	public void updateRoyalty(Royalty updatedRoyalty) {
		royaltyRepository.save(updatedRoyalty);
	}
	public void deleteRoyalty(int id) {
		royaltyRepository.deleteById(id);
	}
	public List<Royalty> searchByartistId(int artistId){
	     return royaltyRepository.findByArtistId(artistId);
	}
	public List<Royalty> searchBysongId(int songId){
	     return royaltyRepository.findBySongId(songId);
	}
	
	 private double calculateRoyaltyAmount(long streams) {
	        if (streams <= 10000) {
	            return streams * 0.002;
	        } else if (streams <= 50000) {
	            return 10000 * 0.002 + (streams - 10000) * 0.005;
	        } else {
	            return 10000 * 0.002 + (50000 - 10000) * 0.005 + (streams - 50000) * 0.01;
	        }
	    }
    
    @Transactional
    public void calculateAndStoreRoyalty() {
        List<Streams> streamsList = streamsRepository.findAll();

        for (Streams stream : streamsList) {
            int songId = stream.getSongId();
            long totalStreams = stream.getStreamCount();
            int artistId = stream.getUserId(); // Assuming user_id is artist_id

            double royaltyAmount = calculateRoyaltyAmount(totalStreams);

            // Retrieve existing royalty records (list)
            List<Royalty> existingRoyalties = royaltyRepository.findBySongId(songId);

            if (!existingRoyalties.isEmpty()) {
                // Update all existing royalty records for the song
                for (Royalty existingRoyalty : existingRoyalties) {
                    existingRoyalty.setTotalStreams(totalStreams);
                    existingRoyalty.setRoyaltyAmount(royaltyAmount);
                    existingRoyalty.setCalculatedDate(new Date());
                    existingRoyalty.setStatus("PENDING");

                    royaltyRepository.save(existingRoyalty);
                }
            } else {
                // Create a new record
                Royalty royalty = new Royalty();
                royalty.setSongId(songId);
                royalty.setArtistId(artistId);
                royalty.setTotalStreams(totalStreams);
                royalty.setRoyaltyAmount(royaltyAmount);
                royalty.setCalculatedDate(new Date());
                royalty.setStatus("PENDING");

                royaltyRepository.save(royalty);
            }
        }
    }


    public void processRoyaltyPayment(int royaltyId, int adminId) {
        Optional<Royalty> royaltyOpt = royaltyRepository.findById(royaltyId);
        
        if (royaltyOpt.isPresent()) {
            Royalty royalty = royaltyOpt.get();
            int artistId = royalty.getArtistId();
            
            // Fetch artist details to get manager ID
            Optional<UserDetails> artistDetails = userDetailsRepository.findById(artistId);
            if (artistDetails.isPresent()) {
                UserDetails artist = artistDetails.get();
                int managerId = artist.getManagerId();
                double amount = royalty.getRoyaltyAmount();
                
                // Calculate payments
                double managerShare = (managerId != artistId) ? amount * 0.10 : 0.0;
                double artistShare = amount - managerShare;

                // Fetch admin details
                Optional<UserDetails> adminDetailsOpt = userDetailsRepository.findById(adminId);
                String adminEmail = adminDetailsOpt.map(UserDetails::getEmail).orElse("admin@example.com");
                String adminName = adminDetailsOpt.map(UserDetails::getFirstName).orElse("Admin");

                // Create Transaction for Artist
                Transactions artistTransaction = new Transactions(
                    artistId,    // Receiver: Artist
                    adminId,     // Sender: Admin
                    royaltyId,
                    new Date(),  // Transaction Date
                    artistShare, // Amount: Artist's Share
                    artistId,    // Manager ID: Null (since this is for the artist)
                    "CREDIT"
                );
                transactionService.addTransaction(artistTransaction);

                // Send Email to Artist (Receiver)
                emailService.sendPaymentReceivedEmail(artist.getEmail(), artist.getFirstName(), artistShare);

                // Send Email to Admin (Sender)
                emailService.sendPaymentSentEmail(adminEmail, adminName, artistShare, artist.getFirstName());

                // Create Transaction for Manager (if applicable)
                if (managerShare > 0) {
                    Optional<UserDetails> managerDetailsOpt = userDetailsRepository.findById(managerId);
                    if (managerDetailsOpt.isPresent()) {
                        UserDetails manager = managerDetailsOpt.get();

                        Transactions managerTransaction = new Transactions(
                            managerId,    // Receiver: Manager
                            adminId,      // Sender: Admin
                            royaltyId,
                            new Date(),   // Transaction Date
                            managerShare, // Amount: Manager's Share
                            managerId,    // Manager ID: Set for the manager's transaction
                            "CREDIT"
                        );
                        transactionService.addTransaction(managerTransaction);

                        // Send Email to Manager (Receiver)
                        emailService.sendPaymentReceivedEmail(manager.getEmail(), manager.getFirstName(), managerShare);

                        // Send Email to Admin (Sender) for Manager's Share
                        emailService.sendPaymentSentEmail(adminEmail, adminName, managerShare, manager.getFirstName());
                    }
                }

                // Update Royalty Status
                royalty.setStatus("PAID");
                royaltyRepository.save(royalty);
            }
        }
    }
}