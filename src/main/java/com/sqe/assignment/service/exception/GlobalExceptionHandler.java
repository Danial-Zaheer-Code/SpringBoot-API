package com.sqe.assignment.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 1. Duplicate value (e.g., unique constraint)
	@ExceptionHandler(DuplicateValueException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateValue(DuplicateValueException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage());
	}

	// 2. Invalid reference (e.g., foreign key constraint)
	@ExceptionHandler(InvalidReferenceException.class)
	public ResponseEntity<ErrorResponse> handleInvalidReference(InvalidReferenceException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// 3. Invalid data (type mismatch, not null, check constraint)
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ErrorResponse> handleInvalidData(InvalidDataException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// 4. Generic database exception
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ErrorResponse> handleDatabase(DatabaseException ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	// 5. Fallback for any unexpected exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
	}

	// Utility method to build consistent error responses
	private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
		return ResponseEntity
				.status(status)
				.body(new ErrorResponse(status.value(), message));
	}
}
