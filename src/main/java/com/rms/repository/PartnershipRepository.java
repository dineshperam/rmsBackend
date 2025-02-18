package com.rms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.model.Partnership;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Integer> {
	
	Optional<Partnership> findByArtistIdAndManagerId(int artistId, int managerId);
    List<Partnership> findByManagerIdAndStatus(int managerId, String status);
    
    Optional<Partnership> findByArtistIdAndStatus(int artistId, String status);
    
    Optional<Partnership> findByArtistIdAndManagerIdAndStatus(int artistId, int managerId, String status);

    @Query("SELECT p FROM Partnership p WHERE p.artistId = :artistId and p.status = 'ACCEPTED' ORDER BY p.startDate DESC LIMIT 1")
    Optional<Partnership> findMostRecentByArtistId(@Param("artistId") int artistId);

	
}
