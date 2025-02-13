package com.rms.controller;
 
 
import java.util.List;
import java.util.NoSuchElementException;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.rms.model.Royalty;
 
import com.rms.service.RoyaltyService;
 
@RestController
@RequestMapping("/royalty")
 
public class RoyaltyController {
	@Autowired
	private RoyaltyService royaltyService;
	
	@GetMapping("/royaltyList")
	public List<Royalty> RoyaltyList(){
		return royaltyService.showRoyalty();
	}
	@GetMapping("/searchId/{id}")
	public ResponseEntity<Royalty> searchRoyaltyById(@PathVariable int id){
		try {
			Royalty royalty = royaltyService.searchRoyaltyById(id);
			return new ResponseEntity<Royalty>(royalty, HttpStatus.OK);
		}
		catch(NoSuchElementException e) {
			return new ResponseEntity<Royalty>(HttpStatus.NOT_FOUND);
		}
	}
		@PostMapping("/addRoyalty")
		public void addRoyalty(@RequestBody Royalty royalty) {
			royaltyService.addRoyalty(royalty);
		}
		@PutMapping("/updateRoyalty")
		public void updateRoyalty(@RequestBody Royalty updatedRoyalty) {
			royaltyService.updateRoyalty(updatedRoyalty);
		}
		@DeleteMapping("/deleteRoyalty/{id}")
		public void deleteRoyalty(@PathVariable int id) {
			royaltyService.deleteRoyalty(id);
		}
		@GetMapping("/searchArtistId/{id}")
		public ResponseEntity<List<Royalty>> searchArtistById(@PathVariable int id){
			try {
            List<Royalty> royalty= royaltyService.searchByartistId(id);
				return new ResponseEntity<List<Royalty>>(royalty, HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				return new ResponseEntity<List<Royalty>>( HttpStatus.NOT_FOUND);
			}
		}
		@GetMapping("/searchSongId/{id}")
		public ResponseEntity<List<Royalty>>searchSongById(@PathVariable int id){
			try {
            List<Royalty> royalty= royaltyService.searchBysongId(id);
				return new ResponseEntity<List<Royalty>>(royalty, HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				return new ResponseEntity<List<Royalty>>( HttpStatus.NOT_FOUND);
			}
		}
		
		@PutMapping("/payRoyalty/{id}/{adminId}")
		public ResponseEntity<String> payRoyalty(@PathVariable int id, @PathVariable int adminId) {
		    royaltyService.processRoyaltyPayment(id, adminId);
		    return ResponseEntity.ok("Royalty payment processed successfully.");
		}
		
		@PostMapping("/calculate")
	    public ResponseEntity<String> calculateRoyalty() {
	        royaltyService.calculateAndStoreRoyalty();
	        return ResponseEntity.ok("Royalty calculation completed successfully.");
	    }

}