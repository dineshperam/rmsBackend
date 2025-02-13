package com.rms.exeptions;

public class NotFoundException extends RuntimeException {
	
	public NotFoundException(String message) {
        super(message);
    }

}
