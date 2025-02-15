package com.rms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rms.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
	
	Song findByTitle(String title);
	List<Song> findByArtistId(int artist_id);
	
	
	@Query("SELECT COUNT(s) FROM Song s WHERE s.artistId = :artistId")
    long countByArtistId(Long artistId);

    // Get songs with collaborators
    List<Song> findByCollaboratorsIsNotNull();
    
    @Query("SELECT s.genre, COUNT(s) FROM Song s WHERE s.artistId = :artistId GROUP BY s.genre")
    List<Object[]> getGenreSongCountByArtist(Long artistId);
        
}
