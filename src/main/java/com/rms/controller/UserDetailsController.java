package com.rms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms.model.UserDetails;
import com.rms.repository.UserDetailsRepository;
import com.rms.service.EmailService;
import com.rms.service.OtpService;
import com.rms.service.UserDetailsService;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(origins="*")
public class UserDetailsController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	 @Autowired
	 private EmailService emailService;

    @Autowired
    private OtpService otpService;
    
	
	@PostMapping(value="/addUser")
	public void addUser(@RequestBody UserDetails userDetails) {
//		String encry = EncryptPassword.getCode(userDetails.getPasswordHash());
//		userDetails.setPasswordHash(encry);
		userDetailsService.addUser(userDetails);
	}
	
	@GetMapping(value = "/searchUser/{userid}")
	public ResponseEntity<UserDetails> searchUser(@PathVariable int userid){
		try {
			UserDetails userDetails=userDetailsService.searchUser(userid);
			return new ResponseEntity<UserDetails>(userDetails,HttpStatus.OK);
		}catch (NoSuchElementException e) {
			return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);		
		
	 }
		
	}
	
	@PutMapping(value = "/updateUser")
	 public void updateUser(@RequestBody UserDetails userDetails) {
		userDetailsService.updateUser(userDetails);
	}
	
	@DeleteMapping(value = "/deleteUser/{userid}")
	public void deleteUser(@PathVariable int userid) {
		userDetailsService.deleteUser(userid);
	}
	
	@GetMapping(value = "/showUser")
	public List<UserDetails>showUser(){
		return userDetailsService.showUsers();
		
	}
	
//	@GetMapping(value = "/login/{user}/{pass}")
//	public String login(@PathVariable String user, @PathVariable String pass) {
//	    UserDetails checkUser = userDetailsService.searchByUseName(user);
//	    if (checkUser != null) {
//	        String hashedPass = EncryptPassword.getCode(pass); 
//	        if (checkUser.getPasswordHash().equals(hashedPass)) {
//	            return "Login successful!"; 
//	        }
//	    }
//	    return "Incorrect credentials!";
//	}
//	

	@GetMapping(value = "/login/{user}/{pass}")
	public ResponseEntity<?	> login(@PathVariable String user, @PathVariable String pass) {
	    UserDetails checkUser = userDetailsService.searchByUseName(user);
	    if (checkUser != null) {
	      //  String hashedPass = EncryptPassword.getCode(pass); 
	        if (checkUser.getPassword().equals(pass)) {
	        	Map<String, Object> response = new HashMap<>();
	            response.put("user", checkUser);
	            return ResponseEntity.ok(response); 
	        }
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials!");
	}
	
	@GetMapping(value = "/searchByUserName/{user}")
	public ResponseEntity<UserDetails> searchByUserName(@PathVariable String user){
		try {
			UserDetails userDetails=userDetailsService.searchByUseName(user);
			return new ResponseEntity<UserDetails>(userDetails,HttpStatus.OK);
		}catch (NoSuchElementException e) {
			return new ResponseEntity<UserDetails>(HttpStatus.NOT_FOUND);		
		
	 }
		
	}
	
	@GetMapping(value="/getArtists")
	public ResponseEntity<List<UserDetails>> getArtists(){
		try {
			List<UserDetails> userDetails = userDetailsService.findArtists();
			return new ResponseEntity<List<UserDetails>>(userDetails, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<List<UserDetails>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/getManagers")
	public ResponseEntity<List<UserDetails>> getManagers(){
		try {
			List<UserDetails> userDetails = userDetailsService.findManagers();
			return new ResponseEntity<List<UserDetails>>(userDetails, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<List<UserDetails>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/getAdmins")
	public ResponseEntity<List<UserDetails>> getAdmins(){
		try {
			List<UserDetails> userDetails = userDetailsService.findAdmins();
			return new ResponseEntity<List<UserDetails>>(userDetails, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<List<UserDetails>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/getArtistUnderManager/{id}")
	public ResponseEntity<List<UserDetails>> getArtistUnderManager(@PathVariable int id){
		try {
			List<UserDetails> userDetails = userDetailsService.findArtistsUnderManager(id);
			return new ResponseEntity<List<UserDetails>>(userDetails, HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<List<UserDetails>>(HttpStatus.NOT_FOUND);
		}
	}
	
	 @GetMapping("/current")
	    public ResponseEntity<UserDetails> getCurrentUser(){
	        return ResponseEntity.ok(userDetailsService.getCurrentLoggedInUser());
	    }
	 
	 // Endpoint to send OTP
	    @PostMapping("/forgotPassword")
	    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> payload) {
	        String username = payload.get("username");
	        UserDetails user = userDetailsRepository.findByUsername(username);

	        if (user == null) {
	            return ResponseEntity.badRequest().body("User not found");
	        }

	        String otp = otpService.generateOtp(username);
	        emailService.sendOtpEmail(user.getEmail(), otp);

	        return ResponseEntity.ok("OTP sent successfully");
	    }

	    // Endpoint to verify OTP
	    @PostMapping("/verifyOtp")
	    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, Object> payload) {
	        String username = (String) payload.get("username");
	        String otp = (String) payload.get("otp");

	        if (otpService.validateOtp(username, otp)) {
	            otpService.clearOtp(username);
	            return ResponseEntity.ok("OTP verified successfully");
	        }
	        return ResponseEntity.badRequest().body("Invalid OTP");
	    }
	 
	 @PutMapping("/updatePassword")
	    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> payload) {
	        String username = payload.get("username");
	        String newPassword = payload.get("newPassword");
	        String otp = payload.get("otp");

	        System.out.println("🔹 Received request to update password for: " + username);
	        System.out.println("🔹 Provided OTP: " + otp);

	        UserDetails user = userDetailsRepository.findByUsername(username);
	        if (user == null) {
	            System.out.println("❌ User not found: " + username);
	            return ResponseEntity.badRequest().body("User not found");
	        }

	        System.out.println("🔹 Validating OTP for: " + username);
	        if (!otpService.validateOtp(username, otp)) {
	            System.out.println("❌ OTP validation failed for: " + username);
	            return ResponseEntity.badRequest().body("Invalid OTP");
	        }

	        System.out.println("✅ OTP validation successful for: " + username);
	        System.out.println("🔹 Updating password...");
	        
	        user.setPassword(passwordEncoder.encode(newPassword));
	        user.setFirstLogin(false);
	        userDetailsRepository.save(user);
	        

	        System.out.println("🔴 Clearing OTP after successful password update...");
	        otpService.clearOtp(username);
	        
	        

	        return ResponseEntity.ok("Password updated successfully");
	    }
	 
	 @PutMapping("/updateStatus/{userId}")
	 public ResponseEntity<?> updateUserStatus(@PathVariable int userId) {
	     Optional<UserDetails> optionalUser = userDetailsRepository.findById(userId);

	     if (optionalUser.isPresent()) {
	         UserDetails user = optionalUser.get();
	         
	         // Toggle isActive status correctly
	         boolean newStatus = !user.isActive();  
	         user.setActive(newStatus);

	         userDetailsRepository.save(user); // Save updated status

	         return ResponseEntity.ok("User " + (newStatus ? "Retrieved" : "Deleted") + " successfully.");
	     }
	     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	 }

	
}
