package com.sqe.assignment.service.util;

import com.sqe.assignment.service.exception.DatabaseException;
import com.sqe.assignment.service.exception.DuplicateValueException;
import com.sqe.assignment.service.exception.InvalidDataException;
import com.sqe.assignment.service.exception.InvalidReferenceException;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class SQLExceptionTranslator {
	public static void translateAndThrow(SQLException e) {
		if (e instanceof SQLIntegrityConstraintViolationException) {
			String sqlState = e.getSQLState();
			String msg = e.getMessage().toLowerCase();

			// Unique constraint violations
			if ("23000".equals(sqlState) || msg.contains("unique") || msg.contains("duplicate")) {
				throw new DuplicateValueException("A book with this title already exists.");
			}

			// Foreign key violations
			if (msg.contains("foreign key") || msg.contains("fk")) {
				throw new InvalidReferenceException("The specified member does not exist.");
			}

			// Not-null violations
			if (msg.contains("not-null") || msg.contains("null")) {
				throw new InvalidDataException("A required field is missing.");
			}

			throw new InvalidDataException("The request violates a database constraint.");
		}

		if (e instanceof SQLDataException) {
			throw new InvalidDataException("Invalid data format or value.");
		}

		throw new DatabaseException("Database error occurred.", e);
	}
}
