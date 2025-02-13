package com.rms.service;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.model.Streams;
import com.rms.repository.StreamsRepository;

@Service
public class StreamsService {
 
	@Autowired
	private StreamsRepository streamRepository;
	
	
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
	
}
