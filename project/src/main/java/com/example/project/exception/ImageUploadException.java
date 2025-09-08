package com.example.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageUploadException extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	public ImageUploadException(String message) {
        super(message);
    }
}
