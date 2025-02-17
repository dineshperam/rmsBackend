package com.rms.repository;
 
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rms.model.Streams;

import jakarta.transaction.Transactional;

public interface StreamsRepository extends JpaRepository<Streams, Integer> {
 
	
	List<Streams> findBySongId(int songId);
	
	List<Streams> findTop3ByOrderByStreamCountDesc();
	
	List<Streams> findBySongIdIn(List<Integer> songIds);
	
	List<Streams> findByStatus(String status);
	
	// Get total streams for a specific song
    long countBySongId(int songId);

    // Get streams within a date range (for monthly insights)
    List<Streams> findByStreamDateBetween(Date startDate, Date endDate);
    
 // First, fetch all songIds related to the given royaltyId
    @Query("SELECT r.songId FROM Royalty r WHERE r.id = :royaltyId")
    List<Integer> findSongIdsByRoyaltyId(@Param("royaltyId") int royaltyId);

    // Then, update the streams where the songId matches
    @Transactional
    @Modifying
    @Query("UPDATE Streams s SET s.status = :status WHERE s.songId IN :songIds")
    void updateStatusBySongIds(@Param("songIds") List<Integer> songIds, @Param("status") String status);
    
    
    @Query("SELECT u.firstName, u.lastName, s.artistId, SUM(st.streamCount) as totalStreams " +
            "FROM Song s " +
            "JOIN Streams st ON s.songId = st.songId " +
            "JOIN UserDetails u ON s.artistId = u.userid " +
            "GROUP BY s.artistId, u.firstName, u.lastName " +
            "ORDER BY totalStreams DESC " +
            "LIMIT 5")
     List<Object[]> findTop5ArtistsByTotalStreams();
     
     @Query("SELECT SUM(s.streamCount) FROM Streams s")
     Integer getTotalStreams();
     
     @Query(value = """
    		    SELECT s.artist_id, u.username, MONTH(st.stream_date) AS month, SUM(st.stream_count) AS total_streams
    		    FROM streams st
    		    JOIN songs s ON st.song_id = s.song_id
    		    JOIN users u ON s.artist_id = u.user_id  -- Join with users table to get the artist name
    		    WHERE YEAR(st.stream_date) = :year
    		    GROUP BY s.artist_id, u.username, MONTH(st.stream_date)
    		    ORDER BY MONTH(st.stream_date), total_streams DESC
    		    """, nativeQuery = true)
    		List<Object[]> findTop5ArtistsByStreams(int year);
    		
    
	@Query(value = """
	    SELECT u.user_id, CONCAT(u.firstname, ' ', u.lastname) AS name, 
	           u.email, u.role, 
	           CASE WHEN u.is_active = 1 THEN 'Active' ELSE 'Inactive' END AS status
	    FROM users u
	    WHERE u.role = 'Artist'
	    ORDER BY u.firstname, u.lastname Limit 5
	""", nativeQuery = true)
	List<Object[]> findTop5ArtistsByStreamsDetails();
	
	@Query("SELECT COALESCE(SUM(s.streamCount), 0) FROM Streams s WHERE s.userId = :userId")
    Long getTotalStreamsByUserId(@Param("userId") int userId);
	
	
	@Query("""
	        SELECT COALESCE(SUM(st.streamCount), 0)
	        FROM Streams st
	        JOIN Song s ON st.songId = s.songId
	        WHERE s.artistId IN (SELECT u.userid FROM UserDetails u WHERE u.managerId = :managerId AND u.role = 'Artist')
	    """)
	long countTotalStreamsByManager(@Param("managerId") int managerId);
	
	@Query("SELECT new map(s.title as songName, SUM(st.streamCount) as totalStreams) " +
		       "FROM Streams st JOIN Song s ON st.songId = s.songId " +
		       "WHERE s.artistId = :artistId " +
		       "GROUP BY s.songId, s.title " +
		       "ORDER BY totalStreams DESC " +
		       "LIMIT 5")
		List<Map<String, Object>> findTop5SongsByArtist(@Param("artistId") int artistId);


	@Query("SELECT s.songId, SUM(s.streamCount) as totalStreams FROM Streams s GROUP BY s.songId ORDER BY totalStreams DESC")
    List<Object[]> findTopSongsByStreams();
}

	