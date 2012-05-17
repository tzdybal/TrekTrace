package org.trektrace.db;

public class DatabaseException extends Exception {

	public DatabaseException(Exception e) {
		super(e.getMessage());
	}

}
