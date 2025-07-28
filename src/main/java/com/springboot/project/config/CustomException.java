package com.springboot.project.config;

import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String error;
	HttpStatus status;

	public CustomException(String error, HttpStatus status) {
		this.error = error;
		this.status = status;
	}

}
