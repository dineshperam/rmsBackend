package com.rms.service;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.dtos.ManagerRoyaltyDTO;
import com.rms.dtos.ManagerRoyaltyDetailsDTO;
import com.rms.dtos.SongDTO;
import com.rms.dtos.UserDTO;
import com.rms.model.Royalty;
import com.rms.model.Song;
import com.rms.model.Streams;
import com.rms.model.Transactions;
import com.rms.model.UserDetails;
import com.rms.repository.RoyaltyRepository;
import com.rms.repository.SongRepository;
import com.rms.repository.StreamsRepository;
import com.rms.repository.TransactionRepository;
import com.rms.repository.UserDetailsRepository;

@Service
public class InsightsService {
	
	@Autowired
	private StreamsRepository streamsRepository;
	@Autowired
    private SongRepository songRepository;
	@Autowired
    private TransactionRepository transactionsRepository;
	@Autowired
    private RoyaltyRepository royaltyRepository;
	@Autowired
    private UserDetailsRepository userRepository;
	
	public List<Object[]> getTopArtistsByRevenueUnderManager(int managerId) {
        return userRepository.findTopArtistsByRevenueUnderManager(managerId);
    }
	
	
	//get top 3 streamed songs
	public List<Song> getTopStreamedSongs() {
        List<Streams> topStreams = streamsRepository.findTop3ByOrderByStreamCountDesc();
        List<Integer> songIds = topStreams.stream().map(Streams::getSongId).collect(Collectors.toList());
        return songRepository.findAllById(songIds);
    }
	
	//total streams per artist
	
	public Map<Integer, Long> getTotalStreamsPerArtist() {
        List<Song> allSongs = songRepository.findAll();
        Map<Integer, List<Integer>> artistSongs = allSongs.stream()
            .collect(Collectors.groupingBy(Song::getArtistId, Collectors.mapping(Song::getSongId, Collectors.toList())));

        Map<Integer, Long> artistStreams = new HashMap<>();
        artistSongs.forEach((artistId, songIds) -> {
            long totalStreams = streamsRepository.findBySongIdIn(songIds).stream()
                                .mapToLong(Streams::getStreamCount).sum();
            artistStreams.put(artistId, totalStreams);
        });

        return artistStreams;
    }
	
	//get total transactions per manager
	
	public Map<Integer, Double> getTotalTransactionsPerManager() {
        List<Transactions> allTransactions = transactionsRepository.findAll();

        return allTransactions.stream()
            .collect(Collectors.groupingBy(Transactions::getManagerId, 
                    Collectors.summingDouble(Transactions::getTransactionAmount)));
    }
	
	//get top 3 earning artists (by royalty)
	
	 public Map<Integer, Double> getTopEarningArtists() {
	        List<Royalty> allRoyalties = royaltyRepository.findAll();

	        return allRoyalties.stream()
	            .collect(Collectors.groupingBy(Royalty::getArtistId, 
	                    Collectors.summingDouble(Royalty::getRoyaltyAmount)))
	            .entrySet().stream()
	            .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
	            .limit(3)
	            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	    }
	
	 
	 //get total royalties paid
	 
	 public double getTotalRoyaltiesPaid() {
	        return royaltyRepository.findAll().stream()
	            .mapToDouble(Royalty::getRoyaltyAmount)
	            .sum();
	    }
	 
	// Get Total Songs per Artist
	    public Map<Integer, Long> getTotalSongsPerArtist() {
	        List<Song> allSongs = songRepository.findAll();
	        return allSongs.stream()
	            .collect(Collectors.groupingBy(Song::getArtistId, Collectors.counting()));
	    }
	    
	 // Get Total Streams per Song
	    public Map<Integer, Long> getTotalStreamsPerSong() {
	        List<Song> allSongs = songRepository.findAll();
	        return allSongs.stream()
	            .collect(Collectors.toMap(Song::getSongId, song -> streamsRepository.countBySongId(song.getSongId())));
	    }
	    
	    
	 // Get Monthly Stream Count
	    public Map<Integer, Long> getMonthlyStreamCount(int month, int year) {
	        Calendar cal = Calendar.getInstance();
	        cal.set(year, month - 1, 1, 0, 0, 0);
	        Date startDate = cal.getTime();
	        
	        cal.set(year, month - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
	        Date endDate = cal.getTime();

	        return streamsRepository.findByStreamDateBetween(startDate, endDate).stream()
	            .collect(Collectors.groupingBy(Streams::getSongId, Collectors.summingLong(Streams::getStreamCount)));
	    }
	    
	 // Get Total Revenue Generated per User
	    public Map<Integer, Double> getTotalRevenuePerUser() {
	        return transactionsRepository.findAll().stream()
	            .collect(Collectors.groupingBy(Transactions::getReceiver, Collectors.summingDouble(Transactions::getTransactionAmount)));
	    }
	    
	 // Get Artist with Most Collaborations
	    public List<Integer> getTopArtistsByCollaborations() {
	        return songRepository.findByCollaboratorsIsNotNull().stream()
	            .collect(Collectors.groupingBy(Song::getArtistId, Collectors.counting()))
	            .entrySet().stream()
	            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
	            .limit(3)
	            .map(Map.Entry::getKey)
	            .toList();
	    }
	    
	    
	    public List<UserDetails> getActiveArtists() {
	        return userRepository.findActiveArtists();
	    }

	    public List<UserDetails> getActiveManagers() {
	        return userRepository.findActiveManagers();
	    }
	    
	    public List<ManagerRoyaltyDTO> getTop5ManagersByTotalRoyalty() {
	        List<Object[]> results = userRepository.findTop5ManagersByTotalRoyalty();
	        return results.stream()
	                .map(obj -> new ManagerRoyaltyDTO(
	                        (Integer) obj[0],  // managerId
	                        (String) obj[1],   // firstName
	                        (String) obj[2],   // lastName
	                        (String) obj[3],   // username
	                        (Double) obj[4]))  // totalRoyalty
	                .collect(Collectors.toList());
	    }
	    
	    public List<Map<String, Object>> getTop5ArtistsByTotalStreams() {
	        List<Object[]> results = streamsRepository.findTop5ArtistsByTotalStreams();
	        List<Map<String, Object>> topArtists = new ArrayList<>();
	        
	        for (Object[] row : results) {
	            Map<String, Object> artistData = new HashMap<>();
	            artistData.put("firstName", row[0]);
	            artistData.put("lastName", row[1]);
	            artistData.put("artistId", row[2]);
	            artistData.put("totalStreams", row[3]);
	            topArtists.add(artistData);
	        }
	        return topArtists;
	    }
	    
	    public Integer getTotalStreams() {
	        return streamsRepository.getTotalStreams();
	    }
	    
	    public Long getActiveArtistsCount() {
	        return userRepository.countActiveArtists();
	    }

	    public Long getActiveManagersCount() {
	        return userRepository.countActiveManagers();
	    }
	    
	    public List<Map<String, Object>> getFormattedTop5ArtistsByMonth(int year) {
	        List<Object[]> results = streamsRepository.findTop5ArtistsByStreams(year);

	        Map<Integer, Map<String, Object>> monthData = new LinkedHashMap<>();
	        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	        for (int i = 1; i <= 12; i++) {
	            Map<String, Object> monthEntry = new LinkedHashMap<>();
	            monthEntry.put("month", monthNames[i - 1]);
	            monthData.put(i, monthEntry);
	        }

	        Map<Integer, List<Object[]>> groupedByMonth = results.stream()
	            .collect(Collectors.groupingBy(row -> (Integer) row[2])); // Group by month

	        groupedByMonth.forEach((month, artistList) -> {
	            List<Object[]> top5Artists = artistList.stream()
	                    .limit(5)
	                    .toList();

	            Map<String, Object> monthEntry = monthData.get(month);
	            for (Object[] artistData : top5Artists) {
	                String artistName = (String) artistData[1]; // Get artist name
	                long totalStreams = ((BigDecimal) artistData[3]).longValue();
	                monthEntry.put(artistName, totalStreams);
	            }
	        });

	        return new ArrayList<>(monthData.values());
	    }
	    
	    public List<Map<String, Object>> getAllArtists() {
	        List<Object[]> results = streamsRepository.findTop5ArtistsByStreamsDetails();
	        
	        return results.stream().map(row -> {
	            Map<String, Object> artistData = new HashMap<>();
	            artistData.put("id", row[0]);
	            artistData.put("name", row[1]);
	            artistData.put("email", row[2]);
	            artistData.put("role", row[3]);
	            artistData.put("status", row[4]);
	            return artistData;
	        }).collect(Collectors.toList());
	    }
	    
	    public List<Map<String, Object>> getTop5ManagersByRoyaltyForYear(int year) {
	        List<Map<String, Object>> response = new ArrayList<>();

	        for (int month = 1; month <= 12; month++) {
	            List<Object[]> topManagers = royaltyRepository.findTop5ManagersByRoyaltyForMonth(year, month);
	            Map<String, Object> monthData = new LinkedHashMap<>();
	            monthData.put("month", new DateFormatSymbols().getMonths()[month - 1]);

	            for (int i = 0; i < topManagers.size(); i++) {
	                Object[] managerData = topManagers.get(i);
	                String managerNameKey = "manager" + (i + 1) + "_name";
	                String royaltyKey = "manager" + (i + 1);

	                monthData.put(managerNameKey, managerData[0]); // Manager Name
	                monthData.put(royaltyKey, managerData[1]); // Total Royalty
	            }

	            response.add(monthData);
	        }

	        return response;
	    }
	    
	    public List<ManagerRoyaltyDetailsDTO> getTop5ManagersByRoyalty(int year) {
	        List<Object[]> results = royaltyRepository.findTop5ManagersByRoyaltyForYear(year);
	        
	        return results.stream().map(obj -> new ManagerRoyaltyDetailsDTO(
	                (String) obj[0],  // fullName
	                (String) obj[1],  // email
	                (String) obj[2],  // role
	                (boolean) obj[3], // isActive
	                (double) obj[4]   // totalRoyalty
	        )).collect(Collectors.toList());
	    }
	    
	    public List<UserDTO> getAllUsers() {
	        return userRepository.findAllUsers();
	    }
	    
	    public long getTotalUserCount() {
	        return userRepository.getTotalUserCount();
	    }
	    
	    public long getTotalActiveUserCount() {
	        return userRepository.getTotalActiveUserCount();
	    }
	    
	    public long getTotalSongsByArtist(Long artistId) {
	        return songRepository.countByArtistId(artistId);
	    }
	    
	    public Double getTotalRoyaltyByArtistId(Long artistId) {
	        return royaltyRepository.getTotalRoyaltyByArtistId(artistId);
	    }
	    
	    public Long getTotalStreamsByUserId(int userId) {
	        return streamsRepository.getTotalStreamsByUserId(userId);
	    }
	    
	 // Insight 1: Total Songs of Artists Under the Manager
	    public Long getTotalSongsByManager(int managerId) {
	        return userRepository.countTotalSongsByManager(managerId);
	    }

	    // Insight 2: Total Streams of Artists Under the Manager
	    public Long getTotalStreamsByManager(int managerId) {
	        return streamsRepository.countTotalStreamsByManager(managerId);
	    }

	    // Insight 3: Manager's Total Revenue
	    public Double getManagerTotalRevenue(int managerId) {
	        return transactionsRepository.getManagerTotalRevenue(managerId);
	    }

	    // Insight 4: Total Revenue of All Artists Under the Manager
	    public Double getTotalRevenueOfArtistsByManager(int managerId) {
	        return royaltyRepository.getTotalRevenueOfArtistsByManager(managerId);
	    }
	    
	    public Map<String, Long> getGenreSongCountByArtist(Long artistId) {
	        List<Object[]> results = songRepository.getGenreSongCountByArtist(artistId);
	        Map<String, Long> genreSongCount = new HashMap<>();
	        
	        for (Object[] row : results) {
	            String genre = (String) row[0];
	            Long count = (Long) row[1];
	            genreSongCount.put(genre, count);
	        }
	        
	        return genreSongCount;
	    }
	    
	    public List<SongDTO> getTopSongsByStreams(int artistId) {
	        List<Song> artistSongs = songRepository.findByArtistId(artistId);
	        List<Integer> artistSongIds = artistSongs.stream().map(Song::getSongId).toList();
	        
	        List<Object[]> topSongs = streamsRepository.findTopSongsByStreams();
	        return topSongs.stream().map(record -> {
	            int songId = (int) record[0];
	            if (artistSongIds.contains(songId)) {
	                Song song = songRepository.findById(songId).orElse(null);
	                if (song != null) {
	                    return new SongDTO(song.getSongId(), song.getReleaseDate(), song.getTitle(), song.getCollaborators(), song.getGenre());
	                }
	            }
	            return null;
	        }).filter(song -> song != null).toList();
	    }
}
