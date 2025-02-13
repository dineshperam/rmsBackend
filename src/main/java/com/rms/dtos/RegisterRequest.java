package com.rms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String address;

    private String role;
    
    private String password;
    
    private String passwordHash;
    
    private int managerId;
    
}
