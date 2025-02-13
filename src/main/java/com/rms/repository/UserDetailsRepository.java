package com.rms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.dtos.UserDTO;
import com.rms.model.UserDetails;

import jakarta.transaction.Transactional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>{
	long countByUsernameAndPassword(String username,String password);
	UserDetails findByUsername(String userName);
	List<UserDetails> findByRole(String role);
	
	Optional<UserDetails> findByEmail(String email);
	
	@Modifying
    @Transactional
    @Query("UPDATE UserDetails u SET u.isActive = false WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") Long userId);
	
	List<UserDetails> findByManagerId(int managerId);
	
	@Query("SELECT u FROM UserDetails u WHERE u.role = 'Artist' and u.isActive = true ")
	List<UserDetails> findActiveArtists();
	
	@Query("SELECT u FROM UserDetails u WHERE u.role = 'Manager' and u.isActive = true ")
    List<UserDetails> findActiveManagers();
	
	@Query("""
		    SELECT u.managerId, u.firstName, u.lastName, u.username, SUM(r.royaltyAmount) as totalRoyalty
		    FROM UserDetails u
		    JOIN Royalty r ON u.userid = r.artistId
		    WHERE u.role = 'Artist'
		    GROUP BY u.managerId, u.firstName, u.lastName, u.username
		    ORDER BY totalRoyalty DESC
		    LIMIT 5
		""")
		List<Object[]> findTop5ManagersByTotalRoyalty();

	    
	
	    @Query("SELECT COUNT(u) FROM UserDetails u WHERE u.role = 'Artist' AND u.isActive = true")
	    Long countActiveArtists();

	    @Query("SELECT COUNT(u) FROM UserDetails u WHERE u.role = 'Manager' AND u.isActive = true")
	    Long countActiveManagers();
	    
	    @Query("SELECT new com.rms.dtos.UserDTO(u.userid, CONCAT(u.firstName, ' ', u.lastName), u.email, u.role, u.isActive) FROM UserDetails u")
	    List<UserDTO> findAllUsers();
	    
	    @Query("SELECT COUNT(u) FROM UserDetails u")
	    long getTotalUserCount();
	    
	    @Query("SELECT COUNT(u) FROM UserDetails u WHERE u.isActive = true")
	    long getTotalActiveUserCount();
	    
	    
}