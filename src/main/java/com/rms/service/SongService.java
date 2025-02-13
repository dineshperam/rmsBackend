package com.rms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Song;
import com.rms.repository.SongRepository;

@Service
public class SongService {
	
	@Autowired
	private SongRepository songRepository;
	
	
	public List<Song> songsList(){
		return songRepository.findAll();
	}
	
	public Song searchSongById(int songId) {
		return songRepository.findById(songId).get();
	}
	
	public void addSong(Song song) {
		songRepository.save(song);
	}
	
	public void updateSong(Song updatedSong) {
		songRepository.save(updatedSong);
	}
	
	public void deleteSong(int id) {
		songRepository.deleteById(id);
	}
	
	public Song searchByTitle(String title) {
		return songRepository.findByTitle(title);
	}
	
	public List<Song> searchByArtistId(int id) {
		return songRepository.findByArtistId(id);
	}
	
	
}
