package com.example.project.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedFileTypeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
