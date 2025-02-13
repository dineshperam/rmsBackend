package com.rms.controller;

import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms.dtos.LoginRequest;
import com.rms.dtos.Response;
import com.rms.service.UserDetailsService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	 @PostMapping("/login")
	    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest) {
	        return ResponseEntity.ok(userDetailsService.loginUser(loginRequest));
	    }
	 
//	 @PostMapping("/register")
//	    public ResponseEntity<Response> registerUser(@RequestBody UserDetails registerRequest) {
//	        return ResponseEntity.ok(userDetailsService.registerUser(registerRequest));
//	    }
//	 
//	
}
