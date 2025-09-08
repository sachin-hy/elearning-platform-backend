package com.example.project.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

	
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	    }

	
	    @ExceptionHandler(ConflictException.class)
	    public ResponseEntity<String> handleConflict(ConflictException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	    }

	  
	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	    }

	  
	    @ExceptionHandler(NumberFormatException.class)
	    public ResponseEntity<String> handleNumberFormat(NumberFormatException ex) {
	        return new ResponseEntity<>("Invalid ID format. Please provide a numeric ID.", HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(InternalServerError.class)
	    public ResponseEntity<String> handleInternalServer(InternalServerError ex)
	    {
	    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public ResponseEntity<String> handleFileSizeException(MaxUploadSizeExceededException ex)
	    {
	    	return new ResponseEntity<>("File size exceeds the maximum limit. Please upload a file smaller than 500MB.",HttpStatus.PAYLOAD_TOO_LARGE);
	    }
	    
	    @ExceptionHandler(UnsupportedFileTypeException.class)
	    public ResponseEntity<String> handleMediaTypeException(UnsupportedFileTypeException ex)
	    {
	    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	        String errorMessage = ex.getBindingResult()
	                                .getFieldError()
	                                .getDefaultMessage();
	        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	    }
	    
	    
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex)
	    {
	    	String errorMessage = ex.getMessage();
	    	return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
}
