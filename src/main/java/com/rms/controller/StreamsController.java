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

import com.rms.model.Streams;
import com.rms.service.StreamsService;

 
 
@RestController
@RequestMapping("/stream")
@CrossOrigin(origins="*")
public class StreamsController {
 
	@Autowired
	private StreamsService streamService;
	
	@GetMapping("/streamList")
	public List<Streams> StreamList(){
		return streamService.showStream();
	}
	@GetMapping("/searchId/{id}")
	public ResponseEntity<Streams> searchStreamById(@PathVariable int id){
		try {
			Streams stream = streamService.searchStreamById(id);
			return new ResponseEntity<Streams>(stream, HttpStatus.OK);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<Streams>(HttpStatus.NOT_FOUND);
		}
	}
		@PostMapping("/addStream")
		public void addStream(@RequestBody Streams stream) {
			streamService.addStream(stream);
		}
		@PutMapping("/updateStream")
		public void updateStream(@RequestBody Streams updatedStream) {
			streamService.updateStream(updatedStream);
		}
		@DeleteMapping("/deleteStream/{id}")
		public void deleteStream(@PathVariable int id) {
			streamService.deleteStream(id);
		}
		
		@GetMapping("/searchSongId/{id}")
		public ResponseEntity<List<Streams>>searchSongById(@PathVariable int id){
			try {
            List<Streams> stream= streamService.searchBysongId(id);
				return new ResponseEntity<List<Streams>>(stream, HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				return new ResponseEntity<List<Streams>>( HttpStatus.NOT_FOUND);
			}
		}
		
		@GetMapping("/in-progress")
	    public List<Streams> getInProgressStreams() {
	        return streamService.getInProgressStreams();
	    }
		
}
 