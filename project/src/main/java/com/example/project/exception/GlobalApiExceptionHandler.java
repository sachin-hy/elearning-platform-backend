package com.example.project.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
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
}
