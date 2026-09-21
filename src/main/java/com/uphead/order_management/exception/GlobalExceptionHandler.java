package com.uphead.order_management.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.uphead.order_management.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	 private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ErrorResponse> handleResourceNotFound(
	            ResourceNotFoundException ex, HttpServletRequest request) {
		 
		 log.warn("Resource not found: {}", ex.getMessage());
		 ErrorResponse response = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.NOT_FOUND.value(),
	                HttpStatus.NOT_FOUND.getReasonPhrase(),
	                ex.getMessage(),
	                request.getRequestURI(),
	                null
	        );

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(response);
	 }
	 
	 @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ErrorResponse> handleIllegalArgument(
	            IllegalArgumentException ex,
	            HttpServletRequest request
	    ) {

	        log.warn("Invalid request: {}", ex.getMessage());

	        ErrorResponse response = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.BAD_REQUEST.value(),
	                HttpStatus.BAD_REQUEST.getReasonPhrase(),
	                ex.getMessage(),
	                request.getRequestURI(),
	                null
	        );

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(response);
	    }

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse> handleValidationException(
	            MethodArgumentNotValidException ex,
	            HttpServletRequest request
	    ) {

	        List<String> errors = ex.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .map(error ->
	                        error.getField() + ": " + error.getDefaultMessage()
	                )
	                .toList();

	        ErrorResponse response = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.BAD_REQUEST.value(),
	                HttpStatus.BAD_REQUEST.getReasonPhrase(),
	                "Validation failed",
	                request.getRequestURI(),
	                errors
	        );

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(response);
	    }

	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<ErrorResponse> handleConstraintViolation(
	            ConstraintViolationException ex,
	            HttpServletRequest request
	    ) {

	        List<String> errors = ex.getConstraintViolations()
	                .stream()
	                .map(violation ->
	                        violation.getPropertyPath()
	                                + ": "
	                                + violation.getMessage()
	                )
	                .toList();

	        ErrorResponse response = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.BAD_REQUEST.value(),
	                HttpStatus.BAD_REQUEST.getReasonPhrase(),
	                "Validation failed",
	                request.getRequestURI(),
	                errors
	        );

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(response);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ErrorResponse> handleGenericException(
	            Exception ex,
	            HttpServletRequest request
	    ) {

	        log.error(
	                "Unexpected error occurred at {}",
	                request.getRequestURI(),
	                ex
	        );

	        ErrorResponse response = new ErrorResponse(
	                LocalDateTime.now(),
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
	                "An unexpected error occurred",
	                request.getRequestURI(),
	                null
	        );

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(response);
	    }

}
