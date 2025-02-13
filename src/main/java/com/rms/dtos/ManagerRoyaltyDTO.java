package com.rms.dtos;

import lombok.Data;

@Data
public class ManagerRoyaltyDTO {
	
	private int managerId;
    private String firstName;
    private String lastName;
    private String username;
    private double totalRoyalty;
    
    public ManagerRoyaltyDTO(int managerId, String firstName, String lastName, String username, double totalRoyalty) {
        this.managerId = managerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.totalRoyalty = totalRoyalty;
    }
}
