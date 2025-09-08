package com.example.project.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

	
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
		 log.error("Resource not found: {}", ex.getMessage(), ex);
	       
		 return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	    }

	
	    @ExceptionHandler(ConflictException.class)
	    public ResponseEntity<String> handleConflict(ConflictException ex) {
	    	 log.error("Conflict error: {}", ex.getMessage(), ex);
	         
	    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	    }

	  
	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
	    	 log.warn("Bad request error: {}", ex.getMessage(), ex);
	         
	    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	    }

	  
	    @ExceptionHandler(NumberFormatException.class)
	    public ResponseEntity<String> handleNumberFormat(NumberFormatException ex) {
	    	 
	    	log.error("Number FormatException : {}",ex.getMessage());
	    	return new ResponseEntity<>("Invalid ID format. Please provide a numeric ID.", HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(InternalServerError.class)
	    public ResponseEntity<String> handleInternalServer(InternalServerError ex)
	    {
	    	 log.error("Internal server error: {}", ex.getMessage(), ex);
		         
	    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public ResponseEntity<String> handleFileSizeException(MaxUploadSizeExceededException ex)
	    {
	    	  log.warn("File size exceeded: {}", ex.getMessage(), ex);
	          
	    	return new ResponseEntity<>("File size exceeds the maximum limit. Please upload a file smaller than 500MB.",HttpStatus.PAYLOAD_TOO_LARGE);
	    }
	    
	    @ExceptionHandler(UnsupportedFileTypeException.class)
	    public ResponseEntity<String> handleMediaTypeException(UnsupportedFileTypeException ex)
	    {
	    	log.warn("Unsupported file type: {}", ex.getMessage(), ex);
	        
	    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
	       
	    	log.warn("Validation errors in request body.", ex);
	        
	    	String errorMessage = ex.getBindingResult()
	                                .getFieldError()
	                                .getDefaultMessage();
	        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	    }
	    
	    
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex)
	    {
	    	
	    	String errorMessage = ex.getMessage();
	    	log.error("IllegealArgumentException : {}" ,errorMessage );
	    	return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
	    }
}
