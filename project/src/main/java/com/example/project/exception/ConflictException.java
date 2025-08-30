package com.example.project.exception;


import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ConflictException(String message) {
		super(message);
	}
}
