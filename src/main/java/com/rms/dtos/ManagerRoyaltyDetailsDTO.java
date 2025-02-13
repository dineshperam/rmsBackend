package com.rms.dtos;

public class ManagerRoyaltyDetailsDTO {
	
	 private String fullName;
	    private String email;
	    private String role;
	    private boolean isActive;
	    private double totalRoyalty;

	    public ManagerRoyaltyDetailsDTO(String fullName, String email, String role, boolean isActive, double totalRoyalty) {
	        this.fullName = fullName;
	        this.email = email;
	        this.role = role;
	        this.isActive = isActive;
	        this.totalRoyalty = totalRoyalty;
	    }

	    // Getters and Setters
	    public String getFullName() { return fullName; }
	    public void setFullName(String fullName) { this.fullName = fullName; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getRole() { return role; }
	    public void setRole(String role) { this.role = role; }

	    public boolean isActive() { return isActive; }
	    public void setActive(boolean active) { isActive = active; }

	    public double getTotalRoyalty() { return totalRoyalty; }
	    public void setTotalRoyalty(double totalRoyalty) { this.totalRoyalty = totalRoyalty; }

}
