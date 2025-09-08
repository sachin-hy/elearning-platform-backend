package com.example.project.exception;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.project.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

	
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
	        log.error("Resource not found: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	    }

	
	    @ExceptionHandler(ConflictException.class)
	    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
	        log.error("Conflict error: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	    }

	  
	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
	        log.warn("Bad request error: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }

	  
	    @ExceptionHandler(NumberFormatException.class)
	    public ResponseEntity<ErrorResponse> handleNumberAndIllegalArgs(RuntimeException ex) {
	        log.warn("Invalid request data: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(InternalServerError.class)
	    public ResponseEntity<ErrorResponse> handleInternalServer(InternalServerError ex) {
	        log.error("Internal server error: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public ResponseEntity<ErrorResponse> handleFileSizeException(MaxUploadSizeExceededException ex) {
	        log.warn("File size exceeded: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse("File size exceeds the maximum limit. Please upload a file smaller than 500MB.", HttpStatus.PAYLOAD_TOO_LARGE, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.PAYLOAD_TOO_LARGE);
	    }
	    
	    @ExceptionHandler(UnsupportedFileTypeException.class)
	    public ResponseEntity<ErrorResponse> handleMediaTypeException(UnsupportedFileTypeException ex) {
	        log.warn("Unsupported file type: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    	 log.warn("Validation errors in request body.", ex);
	         
	         // Get the first error from the BindingResult
	         FieldError firstError = ex.getBindingResult().getFieldError();
	         
	         String errorMessage;
	         if (firstError != null) {
	             errorMessage = firstError.getDefaultMessage();
	         } else {
	             // Fallback message if no field errors are found
	             errorMessage = "Validation failed for the request body.";
	         }
	         
	         ErrorResponse errorResponse = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now());
	         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	    }
	    
	    
	    @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
	        log.warn("Invalid request data: {}", ex.getMessage(), ex);
	        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }
}
