package com.rms.service;
 
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Partnership;
import com.rms.model.Royalty;
import com.rms.model.Streams;
import com.rms.model.Transactions;
import com.rms.model.UserDetails;
import com.rms.repository.PartnershipRepository;
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
	private PartnershipRepository partnershipRepository;
	
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
	        // Get all stream records (assumed to be new records)
	        List<Streams> streamsList = streamsRepository.findByStatus("IN PROGRESS");

	        for (Streams stream : streamsList) {
	            int songId = stream.getSongId();
	            int artistId = stream.getUserId();  // Assuming user_id represents the artist
	            long streamCount = stream.getStreamCount();

	            // Calculate royalty amount based on the stream count
	            double royaltyAmount = calculateRoyaltyAmount(streamCount);

	            // Always create a new royalty record for this stream record
	            Royalty royalty = new Royalty();
	            royalty.setSongId(songId);
	            royalty.setArtistId(artistId);
	            royalty.setTotalStreams(streamCount);
	            royalty.setRoyaltyAmount(royaltyAmount);
	            royalty.setCalculatedDate(new Date());
	            royalty.setStatus("PENDING");

	            royaltyRepository.save(royalty);
	        }
	    }



	    public void processRoyaltyPayment(int royaltyId, int adminId) {
	        Optional<Royalty> royaltyOpt = royaltyRepository.findById(royaltyId);
	        if (royaltyOpt.isEmpty()) {
	            throw new RuntimeException("Royalty not found with ID: " + royaltyId);
	        }

	        Royalty royalty = royaltyOpt.get();
	        int artistId = royalty.getArtistId();

	        Optional<UserDetails> artistDetailsOpt = userDetailsRepository.findById(artistId);
	        if (artistDetailsOpt.isEmpty()) {
	            throw new RuntimeException("Artist not found with ID: " + artistId);
	        }

	        UserDetails artist = artistDetailsOpt.get();
	        int managerId = artist.getManagerId();
	        double totalAmount = royalty.getRoyaltyAmount();

	        // Fetch manager's share from partnership table
	        double managerSharePercentage = partnershipRepository
	                .findByArtistIdAndManagerIdAndStatus(artistId, managerId, "Accepted")
	                .map(Partnership::getPercentage)
	                .orElse(0.0);

	        double managerShare = (managerSharePercentage / 100) * totalAmount;
	        double artistShare = totalAmount - managerShare;

	        Optional<UserDetails> adminDetailsOpt = userDetailsRepository.findById(adminId);
	        String adminEmail = adminDetailsOpt.map(UserDetails::getEmail).orElse("admin@example.com");
	        String adminName = adminDetailsOpt.map(UserDetails::getFirstName).orElse("Admin");

	        // Create Transaction for Artist
	        Transactions artistTransaction = new Transactions(
	            artistId, adminId, royaltyId, new Date(), artistShare, artistId, "CREDIT"
	        );
	        transactionService.addTransaction(artistTransaction);

	        emailService.sendPaymentReceivedEmail(artist.getEmail(), artist.getFirstName(), artistShare);
	        emailService.sendPaymentSentEmail(adminEmail, adminName, artistShare, artist.getFirstName());

	        if (managerShare > 0) {
	            Optional<UserDetails> managerDetailsOpt = userDetailsRepository.findById(managerId);
	            if (managerDetailsOpt.isPresent()) {
	                UserDetails manager = managerDetailsOpt.get();

	                Transactions managerTransaction = new Transactions(
	                    managerId, adminId, royaltyId, new Date(), managerShare, managerId, "CREDIT"
	                );
	                transactionService.addTransaction(managerTransaction);

	                emailService.sendPaymentReceivedEmail(manager.getEmail(), manager.getFirstName(), managerShare);
	                emailService.sendPaymentSentEmail(adminEmail, adminName, managerShare, manager.getFirstName());
	            }
	        }

	        // Update the royalty status to PAID
	        royalty.setStatus("PAID");
	        royaltyRepository.save(royalty);

	        // **Update streams status to "PROCESSED" for related streams**
	        List<Integer> songIds = streamsRepository.findSongIdsByRoyaltyId(royaltyId);
	        if (!songIds.isEmpty()) {
	            streamsRepository.updateStatusBySongIds(songIds, "PROCESSED");
	        }
	    }


}