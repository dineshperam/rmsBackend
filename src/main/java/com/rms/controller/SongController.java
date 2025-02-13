package com.rms.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms.model.Song;
import com.rms.service.SongService;

@RestController
@RequestMapping("/artist")
@CrossOrigin(origins="*")
public class SongController {
	
	@Autowired
	private SongService songService;
	
	@GetMapping("/songsList")
	public List<Song> songsList(){
		return songService.songsList();
	}
	
	@GetMapping("/searchId/{id}")
	public ResponseEntity<Song> searchSongById(@PathVariable int id){
		try {
			Song song = songService.searchSongById(id);
			return new ResponseEntity<Song>(song, HttpStatus.OK);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<Song>( HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addSong")
    public ResponseEntity<String> addSong(@RequestBody Song song) {
        songService.addSong(song);
        return ResponseEntity.ok("Song added successfully");
    }
	
	@PutMapping("/updateSong")
	public void updateSong(@RequestBody Song updatedSong) {
		songService.updateSong(updatedSong);
	}

	@DeleteMapping("/deleteSong/{id}")
	public void deleteSong(@PathVariable int id) {
		songService.deleteSong(id);
	}
	
	@GetMapping("/searchTitle/{title}")
	public ResponseEntity<Song> searchSongByTitle(@PathVariable String title){
		try {
			Song song = songService.searchByTitle(title);
			return new ResponseEntity<Song>(song, HttpStatus.OK);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<Song>( HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/searchByArtistId/{id}")
	public ResponseEntity<List<Song>> searchByArtistId(@PathVariable int id){
		try {
			List<Song> songsList = songService.searchByArtistId(id);
			return new ResponseEntity<List<Song>>(songsList, HttpStatus.OK);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<List<Song>>( HttpStatus.NOT_FOUND);
		}
	}
}
