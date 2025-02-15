package com.rms.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import com.rms.model.Royalty;

@Repository
public interface RoyaltyRepository extends JpaRepository<Royalty, Integer> {
	List<Royalty> findByArtistId(int artistId);
	List<Royalty> findBySongId(int songId);
	@Query("""
	        SELECT u.managerId, SUM(r.royaltyAmount) as totalRoyalty
	        FROM UserDetails u 
	        JOIN Royalty r ON u.userid = r.artistId
	        WHERE u.role = 'Artist'
	        GROUP BY u.managerId
	        ORDER BY totalRoyalty DESC
	        LIMIT 5
	    """)
	List<Object[]> findTop5ManagersByTotalRoyalty();
	
	 @Query("SELECT m.username, SUM(r.royaltyAmount) AS totalRoyalty FROM Royalty r " +
	           "JOIN Song s ON r.songId = s.songId " +
	           "JOIN UserDetails a ON s.artistId = a.userid " +
	           "JOIN UserDetails m ON a.managerId = m.userid " +
	           "WHERE m.role = 'Manager' AND YEAR(r.calculatedDate) = :year AND MONTH(r.calculatedDate) = :month " +
	           "GROUP BY m.username " +
	           "ORDER BY totalRoyalty DESC " +
	           "LIMIT 5")
	    List<Object[]> findTop5ManagersByRoyaltyForMonth(int year, int month);
	    
	    
	    @Query("SELECT CONCAT(m.firstName, ' ', m.lastName) AS fullName, m.email, m.role, m.isActive, SUM(r.royaltyAmount) AS totalRoyalty " +
	            "FROM Royalty r " +
	            "JOIN Song s ON r.songId = s.songId " +
	            "JOIN UserDetails a ON s.artistId = a.userid " +
	            "JOIN UserDetails m ON a.managerId = m.userid " +
	            "WHERE m.role = 'Manager' AND YEAR(r.calculatedDate) = :year " +
	            "GROUP BY m.userid " +
	            "ORDER BY totalRoyalty DESC " +
	            "LIMIT 5")
	     List<Object[]> findTop5ManagersByRoyaltyForYear(int year);
	     
	     @Query("SELECT COALESCE(SUM(r.royaltyAmount), 0) FROM Royalty r WHERE r.artistId = :artistId")
	     Double getTotalRoyaltyByArtistId(Long artistId);
	     
	     @Query("""
	    	        SELECT COALESCE(SUM(r.royaltyAmount), 0)
	    	        FROM Royalty r
	    	        WHERE r.artistId IN (SELECT u.userid FROM UserDetails u WHERE u.managerId = :managerId AND u.role = 'Artist')
	    	    """)
	    	Double getTotalRevenueOfArtistsByManager(@Param("managerId") int managerId);


	
}