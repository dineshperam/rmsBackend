package com.rms.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	
	//Generic
    private int status;
    private String message;
    //for login
    private String token;
    private int userId;
    private boolean isFirstLogin;
    private String role;
    private String expirationTime;
    private boolean isActive;
    private int managerId;
    private String firstName;
    
    public boolean isFirstLogin() {  // Correct boolean getter format
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.isFirstLogin = firstLogin;
    }

}
