package com.rms.dtos;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class SongDTO {
	
	 private int songId;
	    private LocalDate releaseDate;
	    private String title;
	    private String collaborators;
	    private String genre;
	    
	    public SongDTO(int songId, LocalDate releaseDate, String title, String collaborators, String genre) {
	        this.songId = songId;
	        this.releaseDate = releaseDate;
	        this.title = title;
	        this.collaborators = collaborators;
	        this.genre = genre;
	    }
	    
	    // Getters and Setters

}
