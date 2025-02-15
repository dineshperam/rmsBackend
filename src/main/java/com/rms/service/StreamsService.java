package com.rms.service;
 
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Song;
import com.rms.model.Streams;
import com.rms.repository.SongRepository;
import com.rms.repository.StreamsRepository;

@Service
public class StreamsService {
 
	@Autowired
	private StreamsRepository streamRepository;
	
	@Autowired
    private SongRepository songRepository;
	
	
	public List<Streams> showStream(){
		return streamRepository.findAll();
	}
	public Streams searchStreamById(int streamId) {
		return streamRepository.findById(streamId).get();
	}
	public void addStream(Streams stream) {
		streamRepository.save(stream);
	}
	public void updateStream(Streams updatedStream) {
		streamRepository.save(updatedStream);
	}
	public void deleteStream(int id) {
		streamRepository.deleteById(id);
	}
	
	public List<Streams> searchBysongId(int songId){
	     return streamRepository.findBySongId(songId);
}
	
	public List<Streams> getInProgressStreams() {
        return streamRepository.findByStatus("IN PROGRESS");
    }
	
	 private final Random random = new Random();

	    public void insertNewStreams() {
	        List<Song> songs = songRepository.findAll();
	        for (Song song : songs) {
	            int streamCount = 500 + random.nextInt(201);
	            Streams newStream = new Streams(song.getSongId(), streamCount,new Date(), song.getArtistId());
	            streamRepository.save(newStream);
	        }
	    }
}
