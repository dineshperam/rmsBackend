package com.rms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rms.dtos.ManagerRoyaltyDTO;
import com.rms.dtos.ManagerRoyaltyDetailsDTO;
import com.rms.dtos.UserDTO;
import com.rms.model.Song;
import com.rms.model.UserDetails;
import com.rms.service.InsightsService;

@RestController
@RequestMapping("/insights")
@CrossOrigin(origins="*")
public class InsightsController {
	
	@Autowired
	private InsightsService insightsService;
	
	// Get top earning artists
    @GetMapping("/top-earning-artists")
    public Map<Integer, Double> getTopEarningArtists() {
        return insightsService.getTopEarningArtists();
    }
    
    @GetMapping("/top5-managers")
    public ResponseEntity<List<ManagerRoyaltyDTO>> getTop5Managers() {
        List<ManagerRoyaltyDTO> topManagers = insightsService.getTop5ManagersByTotalRoyalty();
        return ResponseEntity.ok(topManagers);
    }

	
	@GetMapping("/top-streamed-songs")
    public List<Song> getTopStreamedSongs() {
        return insightsService.getTopStreamedSongs();
    }
	
	//Get Artists with Most Collaborations
    @GetMapping("/top-artists-by-collaborations")
    public List<Integer> getTopArtistsByCollaborations() {
        return insightsService.getTopArtistsByCollaborations();
    }
    
    @GetMapping("/active-artists")
    public List<UserDetails> getActiveArtists() {
        return insightsService.getActiveArtists();
    }

    @GetMapping("/active-managers")
    public List<UserDetails> getActiveManagers() {
        return insightsService.getActiveManagers();
    }
    
    @GetMapping("/active-artists-count")
    public ResponseEntity<Long> getActiveArtistsCount() {
        return ResponseEntity.ok(insightsService.getActiveArtistsCount());
    }

    @GetMapping("/active-managers-count")
    public ResponseEntity<Long> getActiveManagersCount() {
        return ResponseEntity.ok(insightsService.getActiveManagersCount());
    }
    
    @GetMapping("/top5-artists")
    public ResponseEntity<List<Map<String, Object>>> getTop5Artists() {
        List<Map<String, Object>> topArtists = insightsService.getTop5ArtistsByTotalStreams();
        return ResponseEntity.ok(topArtists);
    }
	
	 //Get Total Streams per Artist
    @GetMapping("/total-streams-per-artist")
    public Map<Integer, Long> getTotalStreamsPerArtist() {
        return insightsService.getTotalStreamsPerArtist();
    }
    
    // Get Total Transactions per Manager
    @GetMapping("/total-transactions-per-manager")
    public Map<Integer, Double> getTotalTransactionsPerManager() {
        return insightsService.getTotalTransactionsPerManager();
    }
    
    
    // Get Total Royalties Paid
    @GetMapping("/total-royalties-paid")
    public double getTotalRoyaltiesPaid() {
        return insightsService.getTotalRoyaltiesPaid();
    }
    
    //Get Total Songs per Artist
    @GetMapping("/total-songs-per-artist")
    public Map<Integer, Long> getTotalSongsPerArtist() {
        return insightsService.getTotalSongsPerArtist();
    }

    // Get Total Streams per Song
    @GetMapping("/total-streams-per-song")
    public Map<Integer, Long> getTotalStreamsPerSong() {
        return insightsService.getTotalStreamsPerSong();
    }

    //Get Monthly Stream Count
    @GetMapping("/monthly-stream-count")
    public Map<Integer, Long> getMonthlyStreamCount(@RequestParam int month, @RequestParam int year) {
        return insightsService.getMonthlyStreamCount(month, year);
    }

    // Get Total Revenue per User
    @GetMapping("/total-revenue-per-user")
    public Map<Integer, Double> getTotalRevenuePerUser() {
        return insightsService.getTotalRevenuePerUser();
    }
    
    @GetMapping("/total-streams-count")
    public Integer getTotalStreams() {
        return insightsService.getTotalStreams();
    }
    
    @GetMapping("/top5artists")
    public List<Map<String, Object>> getTop5Artists(@RequestParam int year) {
        return insightsService.getFormattedTop5ArtistsByMonth(year);
    }
    
    @GetMapping("/top5artists-details")
    public List<Map<String, Object>> getArtists() {
        return insightsService.getAllArtists();
    }
    
    @GetMapping("/top5-managers-monthly")
    public List<Map<String, Object>> getTop5ManagersByRoyaltyForYear(@RequestParam int year) {
        return insightsService.getTop5ManagersByRoyaltyForYear(year);
    }
    
    @GetMapping("/top5-managers-detailsTab")
    public List<ManagerRoyaltyDetailsDTO> getTopManagers(@RequestParam int year) {
        return insightsService.getTop5ManagersByRoyalty(year);
    }
    
    @GetMapping("/admin-getall-users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(insightsService.getAllUsers());
    }
    
    
    @GetMapping("/admin-allusers-count")
    public long getTotalUserCount() {
        return insightsService.getTotalUserCount();
    }
    
    @GetMapping("/active-users-count")
    public long getTotalActiveUserCount() {
        return insightsService.getTotalActiveUserCount();
    }
    
    @GetMapping("/mysongs/{artistId}")
    public long getSongCountByArtist(@PathVariable Long artistId) {
        return insightsService.getTotalSongsByArtist(artistId);
    }
    
    @GetMapping("/totalRevenuebyId/{artistId}")
    public Double getTotalRoyalty(@PathVariable Long artistId) {
        return insightsService.getTotalRoyaltyByArtistId(artistId);
    }
    
    @GetMapping("/totalStreamsById/{userId}")
    public Long getTotalStreams(@PathVariable int userId) {
        return insightsService.getTotalStreamsByUserId(userId);
    }
    
    
}
