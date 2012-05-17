package org.trektrace.db;

import org.trektrace.Configuration;

import net.rim.device.api.database.Database;
import net.rim.device.api.database.DatabaseFactory;
import net.rim.device.api.database.DatabaseIOException;
import net.rim.device.api.database.DatabasePathException;
import net.rim.device.api.io.MalformedURIException;
import net.rim.device.api.io.URI;

public class DatabaseManager {
	private static final URI DB_URI = getURI();
	private static Database db = null;

	private static URI getURI() {
		try {
			return URI.create(Configuration.DB_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean createDatabase() throws DatabaseException {
		if (databaseExists()) {
			openDatabase();
			return false;
		} else {
			try {
				db = DatabaseFactory.create(DB_URI);
				createTables();
				return true;
			} catch (Exception e) {
				throw new DatabaseException(e);
			}
		}
	}

	public static Database getDatabase() throws DatabaseException {
		if (db == null) {
			openDatabase();
		}
		return db;
	}
	
	public static boolean databaseExists() {
		try {
			if (DB_URI == null)
				return false;

			return DatabaseFactory.exists(DB_URI);
		} catch (Exception e) {
			return false;
		}
	}


	private static void openDatabase() throws DatabaseException {
		try {
			URI uri = URI.create(Configuration.DB_FILE);
			db = DatabaseFactory.open(uri);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
	
	private static void createTables() {
		
		
	}
}
