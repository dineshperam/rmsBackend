package com.rms.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Songs")
public class Song {
	
	@Id
	@Column(name="song_id")
	private int songId;
	
	@Column(name="artist_id")
	private int artistId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name="release_date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd") // Ensures correct JSON serialization/deserialization
	private LocalDate releaseDate;
	
	@Column(name="collaborators")
	private String collaborators;
	
	@Column(name="genre")
	private String genre;
	
}
