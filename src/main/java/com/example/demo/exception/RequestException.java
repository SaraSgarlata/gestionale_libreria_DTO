package com.example.demo.exception;

public class RequestException extends RuntimeException{
	
	
	private final String message;
	private final String httpStatus;
	
	
	
	 public RequestException(String message, String httpStatus) {
	        super(); 
	        this.message = message; 
	        this.httpStatus = httpStatus;
	    }



	public String getMessage() {
		return message;
	}



	public String getHttpStatus() {
		return httpStatus;
	}

}
