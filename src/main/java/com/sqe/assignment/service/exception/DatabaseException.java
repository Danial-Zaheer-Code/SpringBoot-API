package com.sqe.assignment.service.exception;

public class DatabaseException extends RuntimeException {
	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
