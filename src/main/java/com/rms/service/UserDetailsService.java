package com.rms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rms.dtos.LoginRequest;
import com.rms.dtos.Response;
import com.rms.exeptions.InvalidCredentialsException;
import com.rms.exeptions.NotFoundException;
import com.rms.model.UserDetails;
import com.rms.repository.UserDetailsRepository;
import com.rms.security.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	
	public void addUser(UserDetails userDetails) {
		userDetailsRepository.save(userDetails);
		
	}
	
	public UserDetails searchUser(int userid) {
		return userDetailsRepository.findById(userid).get();
		
	}
	
	public void updateUser(UserDetails userDetails) {
		userDetailsRepository.save(userDetails);
		
	}
	
	public void deleteUser(int userid) {
		userDetailsRepository.deleteById(userid);
		
	}
	
	
	public List<UserDetails> showUsers() {
		return userDetailsRepository.findAll();
		
	}
	
	public String login(String username,String password) {
		long count=userDetailsRepository.countByUsernameAndPassword(username, password);
		String res="";
		res+=count;
		return res;
	} 
	
	public boolean updateUserDetails(UserDetails userDetails) {
        UserDetails user = userDetailsRepository.findByUsername(userDetails.getUsername());

        if (user == null) {
            return false; // User not found
        }

        // Update only allowed fields
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setMobileNo(userDetails.getMobileNo());
        user.setAddress(userDetails.getAddress());

        userDetailsRepository.save(user);
        return true;
    }	
	
	public UserDetails searchByUseName(String userName) {
		return userDetailsRepository.findByUsername(userName);
		
	}
	
	public List<UserDetails> findArtists(){
		return userDetailsRepository.findByRole("Artist");
	}
	
	public List<UserDetails> findManagers(){
		return userDetailsRepository.findByRole("Manager");
	}
	
	public List<UserDetails> findAdmins(){
		return userDetailsRepository.findByRole("Admin");
	}
	
	public List<UserDetails> findArtistsUnderManager(int managerId){
		return userDetailsRepository.findByManagerId(managerId);
	}
	
	public Response registerUser(UserDetails userDetails) {
		userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		userDetails.isActive();
		userDetailsRepository.save(userDetails);

	        return Response.builder()
	                .status(200)
	                .message("User was successfully registered")
	                .build();
	}

    public Response loginUser(LoginRequest loginRequest) {
    	
    	 UserDetails user = userDetailsRepository.findByEmail(loginRequest.getEmail())
                 .orElseThrow(() -> new NotFoundException("Email Not Found"));

         if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
             throw new InvalidCredentialsException("Password Does Not Match");
         }
         String token = jwtUtils.generateToken(user.getEmail());

         return Response.builder()
                 .status(200)
                 .message("User Logged in Successfully")
                 .role(user.getRole())
                 .token(token)
                 .expirationTime("6 months")
                 .isActive(user.isActive())
                 .isFirstLogin(user.isFirstLogin())
                 .userId(user.getUserid())
                 .managerId(user.getManagerId())
                 .firstName(user.getFirstName())
                 .build();
    	
    }
    
//    public Response firstLoginUser(LoginRequest loginRequest) {
//    	
//   	 UserDetails user = userDetailsRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new NotFoundException("Email Not Found"));
//
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Password Does Not Match");
//        }
//        String token = jwtUtils.generateToken(user.getEmail());
//
//        return Response.builder()
//                .status(200)
//                .message("User Logged in Successfully")
//                .role(user.getRole())
//                .token(token)
//                .expirationTime("6 months")
//                .isActive(true)
//                .isFirstLogin(1)
//                .userId(user.getUserid())
//                .build();
//   	
//   }
    
    public UserDetails getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserDetails user = userDetailsRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found"));

        return user;
    }
}